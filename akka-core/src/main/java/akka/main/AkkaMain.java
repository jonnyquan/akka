package akka.main;

import akka.core.Akka;
import akka.core.AkkaSystemContext;
import akka.msg.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     *
     * @return
     */
    public static Akka initAkka() {
        return new AkkaMain().getAkka();
    }

    private Akka getAkka() {
        return akkaSystem;
    }

    private Akka createSystem(String systemName) {
        return new AkkaSystemContext(systemName);
    }


}
