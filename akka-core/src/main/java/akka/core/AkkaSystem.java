package akka.core;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.enums.RouterGroup;
import akka.params.AskProcessHandler;
import akka.params.DefaultAskProcessHandler;
import akka.params.RegisterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对akka的system简单包装 绑定了集群地址管理器AddressContext   已经集群检测actor
 * 系统初始化时候   生成MsgSenderWrapper之前必须要先进行 @prepareLoadAdd (集群状态初始化等待 以及路由模式地址生成后的某种等待。。暂时找不到原因)
 * <p>
 * Created by ruancl@xkeshi.com on 16/10/9.
 */
public class AkkaSystem extends AbstractAkkaSystem {

    private static final Logger logger = LoggerFactory.getLogger(AkkaSystem.class);


    /**
     * @param systemName
     */
    public AkkaSystem(String systemName) {
        super(systemName);
    }

    /**
     * actor 注册
     *
     * @param registerBean
     * @return
     */
    @Override
    public Akka register(RegisterBean registerBean) {
        ActorRef ref = getSystem().actorOf(registerBean.getPool().props(Props.create(registerBean.gettClass())), registerBean.getName());
        logger.info("register actor:{}", ref.path());
        System.out.println("注册actor:" + ref.path() + "成功========================");
        return this;
    }


    /**
     * (tell模式)
     *
     * @param name
     * @return
     */
    private Sender createTellMsgWrapper(final String name,final RouterGroup routerGroup) {
        return new TellSenderWrapper(
                name,
                routerGroup,
                this);
    }


    /**
     * 使用该功能发消息必须要先启动集群监听@AkSystem
     * <p>
     * (ask模式)
     *
     * @param name
     * @param askProcessHandler
     * @return
     */
    private Sender createAskMsgWrapper(final String name, AskProcessHandler<?, ?> askProcessHandler,final RouterGroup routerGroup) {
        return new AskSenderWrapper<>(
                name,
                askProcessHandler,
                routerGroup,
                this);
    }




    @Override
    public Sender createAskSender(String name, RouterGroup routerGroup) {
        return createAskMsgWrapper(name,new DefaultAskProcessHandler(), routerGroup);
    }

    @Override
    public Sender createAskSender(String name, AskProcessHandler<?, ?> askProcessHandler, RouterGroup routerGroup) {
        return createAskMsgWrapper(name,askProcessHandler, routerGroup);
    }

    @Override
    public Sender createTellSender(String name, RouterGroup routerGroup) {
        return createTellMsgWrapper(name, routerGroup);
    }

    @Override
    public Sender createAskSender(String name) {
        return createAskSender(name,new DefaultAskProcessHandler(), RouterGroup.RANDOM);
    }

    @Override
    public Sender createTellSender(String name) {
        return createTellSender(name, RouterGroup.RANDOM);
    }
}
