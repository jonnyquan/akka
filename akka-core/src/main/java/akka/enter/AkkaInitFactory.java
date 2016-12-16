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

import java.io.File;
import java.io.FileFilter;
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
    private void scanFile(File file,List<RegisterBean> classes){
        if(file.isDirectory()){
            for(File ff : file.listFiles(f->f.getName().endsWith(EXT) || f.isDirectory())){
                scanFile(ff,classes);
            }
        }else{
            try {
                Class clazz = Class.forName(file.getAbsolutePath().replace(ROOT_PATH,"").replace(EXT,"").replace("/","."));
                Actor actor = (Actor) clazz.getAnnotation(Actor.class);
                if(actor == null){
                    return;
                }
                if (clazz.getSuperclass() != AbstractActor.class) {
                    throw new IllegalArgumentException("无效的actor继承类型", new IllegalArgumentException());
                }
                classes.add(new RegisterBean(clazz, actor.name(), actor.pool().getPool(actor.number())));
            } catch (ClassNotFoundException e) {
                logger.error("扫描获取的类路径异常,找不对对应class");
                e.printStackTrace();
            }
        }
    }

    private final String ROOT_PATH = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private final String EXT = ".class";

    protected Optional<List<RegisterBean>> scanPackage() {
        List<RegisterBean> classes = new ArrayList();
        File file = new File(ROOT_PATH);
        scanFile(file,classes);
       return Optional.ofNullable(classes);
    }

    private AkSystem createSystem(String systemName) {
        return createSystem(systemName, true);
    }

    private AkSystem createSystem(String systemName, Boolean withCluster) {
        Config config = ConfigFactory.load();
        ActorSystem system = ActorSystem.create(systemName, config);
        AkSystem akSystem = new AkSystem(system, withCluster);
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
