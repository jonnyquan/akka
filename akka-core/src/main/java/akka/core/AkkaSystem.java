package akka.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actors.ClusterActor;
import akka.enums.RouterStrategy;
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
public class AkkaSystem implements Akka {

    private static final Logger logger = LoggerFactory.getLogger(AkkaSystem.class);

    private final ActorSystem system;


    /**
     * @param system
     * @param withCluster            启动集群监听
     */
    public AkkaSystem(ActorSystem system, Boolean withCluster) {
        this.system = system;
        if (withCluster) {
             system.actorOf(Props.create(ClusterActor.class,AddressStrategy.getClusterAddress()));
        }
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
    public Sender createTellMsgWrapper(final String name,final RouterStrategy routerStrategy) {
        //地址预加载
        AddressStrategy.prepareLoadAdd(this.system,name,routerStrategy);
        return new TellSenderWrapper(
                name,
                routerStrategy);
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
    public Sender createAskMsgWrapper(final String name, AskProcessHandler<?, ?> askProcessHandler,final RouterStrategy routerStrategy) {
        //地址预加载
        AddressStrategy.prepareLoadAdd(this.system,name,routerStrategy);
        return new AskSenderWrapper<>(
                name,
                system.dispatcher(),
                askProcessHandler,
                routerStrategy);
    }


    public ActorSystem getSystem() {
        return system;
    }


    @Override
    public Sender createAskSender(String name, RouterStrategy routerStrategy) {
        return createAskMsgWrapper(name,new DefaultAskProcessHandler(),routerStrategy);
    }

    @Override
    public Sender createAskSender(String name, AskProcessHandler<?, ?> askProcessHandler, RouterStrategy routerStrategy) {
        return createAskMsgWrapper(name,askProcessHandler,routerStrategy);
    }

    @Override
    public Sender createTellSender(String name, RouterStrategy routerStrategy) {
        return createTellMsgWrapper(name,routerStrategy);
    }

    @Override
    public Sender createAskSender(String name) {
        return createAskSender(name,new DefaultAskProcessHandler(),RouterStrategy.RANDOM);
    }

    @Override
    public Sender createTellSender(String name) {
        return createTellSender(name,RouterStrategy.RANDOM);
    }
}
