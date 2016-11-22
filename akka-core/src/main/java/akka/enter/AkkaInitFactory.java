package akka.enter;

import akka.actor.ActorSystem;
import akka.actors.AbstractActor;
import akka.anntations.Actor;
import akka.cluster.Cluster;
import akka.msg.Constant;
import akka.params.AskHandle;
import akka.params.DefaultAskHandle;
import akka.params.RegisterBean;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ruancl@xkeshi.com on 16/10/19.
 */
public class AkkaInitFactory {

    private static final Logger logger = LoggerFactory.getLogger(AkkaInitFactory.class);

    private AkSystem akSystem;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    @Autowired
    private SpringContextUtil springContextUtil;

    public AkkaInitFactory() {
        init();
    }

    private void init() {
        this.akSystem = createSystem(Constant.SYSTEM_NAME, true);
        scanPackage().ifPresent(list ->
                list.forEach(bean -> {
                    this.akSystem.register(bean);
                    logger.info("注册actor:{}成功", bean.getName());
                }));
    }

    private Optional<List<RegisterBean>> scanPackage() {
        List<RegisterBean> classes = new ArrayList();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resourcePatternResolver.getResources(Constant.SCAN_PATH_PATTEN);

            MetadataReaderFactory metadataReaderFactory =
                    new CachingMetadataReaderFactory(new PathMatchingResourcePatternResolver());
            AnnotationTypeFilter filter = new AnnotationTypeFilter(Actor.class);
            for (Resource resource : resources) {
                if (!resource.isReadable()) {
                    continue;
                }
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                if (!filter.match(metadataReader, metadataReaderFactory)) {
                    continue;
                }
                //将标记actor注解的类收集 注册到akkasystem
                String className = metadataReader.getClassMetadata().getClassName();
                Class clazz = Class.forName(className);
                Actor actor = (Actor) clazz.getAnnotation(Actor.class);
                if (clazz.getSuperclass() != AbstractActor.class) {
                    throw new IllegalArgumentException("无效的actor继承类型", new IllegalArgumentException());
                }
                classes.add(new RegisterBean(clazz, actor.name(), actor.pool().getPool(actor.number())));
            }
        } catch (IOException e) {
            logger.error("actor类资源读取io异常");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            logger.error("扫描获取的类路径异常,找不对对应class");
            e.printStackTrace();
        }
        return Optional.ofNullable(classes);
    }

    private AkSystem createSystem(String systemName, SpringContextUtil applicationContextUtil) {
        return createSystem(systemName, true, true, applicationContextUtil);
    }

    private AkSystem createSystem(String systemName, Boolean withCluster) {
        return createSystem(systemName, withCluster, false, null);
    }

    private AkSystem createSystem(String systemName, Boolean withCluster, Boolean withRpc, SpringContextUtil applicationContextUtil) {
        Config config = ConfigFactory.load();
        ActorSystem system = ActorSystem.create(systemName, config);
        AkSystem akSystem = new AkSystem(system, withCluster, withRpc, applicationContextUtil);
        //在节点监听还未成功建立前阻塞消息
        Cluster.get(system).registerOnMemberUp(() ->
                countDownLatch.countDown()
        );
        if (countDownLatch.getCount() == 1) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("actor system创建完毕");
        return akSystem;
    }

    public Object getBean(Class clazzInterface) {
        return akSystem.getBean(clazzInterface);
    }

    /**
     * @param name 该路径与接收消息短的 @actor name保持一致
     */
    public MsgSender createMsgGun(String name) {
        return createMsgGun(name, new DefaultAskHandle());
    }

    /**
     * @param name
     * @param askHandle 自定义ask模式下的 对于请求的各种情况处理
     * @return
     */
    public MsgSender createMsgGun(String name, AskHandle<?, ?> askHandle) {
        akSystem.prepareLoadAdd(name);
        return new MsgGun(name, akSystem, askHandle);
    }
}
