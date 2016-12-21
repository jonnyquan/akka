package akka.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actors.ClusterActor;
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
public class AkkaSystem {

    private static final Logger logger = LoggerFactory.getLogger(AkkaSystem.class);
    private final ActorSystem system;

    private AddressContextImpl addressContextImpl;


    private ActorRef clusterActor;


    /**
     * @param system
     * @param withCluster            启动集群监听
     */
    public AkkaSystem(ActorSystem system, Boolean withCluster) {
        this.system = system;
        this.addressContextImpl = new AddressContextImpl(this.system);
        if (withCluster) {
            clusterActor = system.actorOf(Props.create(ClusterActor.class, addressContextImpl));
        }
    }


    /**
     * 接收方actor引用预加载
     *
     * @param name
     */
    public void prepareLoadAdd(String name) {
        //地址预加载
        addressContextImpl.prepareLoadAdd(name);
    }

    /**
     * actor 注册
     *
     * @param registerBean
     * @return
     */
    public AkkaSystem register(RegisterBean registerBean) {
        ActorRef ref = system.actorOf(registerBean.getPool().props(Props.create(registerBean.gettClass())), registerBean.getName());
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
    public AbstractSenderWrapper createTellMsgWrapper(final String name) {
        return new TellSenderWrapper(
                name,
                addressContextImpl,
                system);
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
    public AbstractSenderWrapper createAskMsgWrapper(final String name, AskProcessHandler<?, ?> askProcessHandler) {
        return new AskSenderWrapper<>(
                name,
                addressContextImpl,
                system,
                askProcessHandler);
    }


    public ActorSystem getSystem() {
        return system;
    }

    /**
     * @param name 该路径与接收消息短的 @actor name保持一致
     */
    public MsgSender createMsgGun(String name) {
        return createMsgGun(name, new DefaultAskProcessHandler());
    }

    /**
     * @param name
     * @param askProcessHandler 自定义ask模式下的 对于请求的各种情况处理
     * @return
     */
    public MsgSender createMsgGun(String name, AskProcessHandler<?, ?> askProcessHandler) {
        this.prepareLoadAdd(name);
        return new MsgSenderImpl(name, this, askProcessHandler);
    }

}
