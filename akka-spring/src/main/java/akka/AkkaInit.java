package akka;

import akka.actors.AbstractActor;
import akka.anntations.Actor;
import akka.main.AkkaMain;
import akka.msg.Constant;
import akka.params.RegisterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Created by ruancl@xkeshi.com on 2016/12/15.
 */
public class AkkaInit extends AkkaMain {

    private static final Logger logger = LoggerFactory.getLogger(AkkaInit.class);

    public final static String SCAN_PATH_PATTEN = "classpath:*/**/*.class";


    @Override
    protected Optional<List<RegisterBean>> scanPackage() {
        List<RegisterBean> classes = new ArrayList();
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resourcePatternResolver.getResources(SCAN_PATH_PATTEN);

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
}
