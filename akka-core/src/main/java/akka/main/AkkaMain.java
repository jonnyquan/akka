package akka.main;

import akka.actors.AbstractActor;
import akka.anntations.Actor;
import akka.core.Akka;
import akka.core.AkkaSystem;
import akka.msg.Constant;
import akka.params.RegisterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by ruancl@xkeshi.com on 16/10/19.
 * 启动入口
 * akka启动
 * actor对象扫描注册
 */
public class AkkaMain {

    private static final Logger logger = LoggerFactory.getLogger(AkkaMain.class);

    private final Akka akkaSystem;

    protected AkkaMain() {
        this.akkaSystem = createSystem(Constant.SYSTEM_NAME);
    }

    /**
     * 启动方法入口
     * @return
     */
    public static Akka initAkka(){
        return new AkkaMain().init().getAkka();
    }

    private Akka getAkka() {
        return akkaSystem;
    }


    private AkkaMain init() {
        scanPackage().ifPresent(list ->
                list.forEach(bean -> {
                    this.akkaSystem.register(bean);
                    logger.info("注册actor:{}成功", bean.getName());
                }));

        return this;
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

    private Akka createSystem(String systemName) {
        return new AkkaSystem(systemName);
    }



}
