package akka.enter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actors.ClusterActor;
import akka.params.AskHandle;
import akka.params.RegisterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统初始化时候   生成MsgSenderWrapper之前必须要先进行 @prepareLoadAdd (集群状态初始化等待 以及路由模式地址生成后的某种等待。。暂时找不到原因)
 * <p>
 * Created by ruancl@xkeshi.com on 16/10/9.
 */
public class AkSystem {

    private static final Logger logger = LoggerFactory.getLogger(AkSystem.class);
    private final ActorSystem system;

    private AddressContex addressContex = new AddressContex();


    private ActorRef clusterActor;


    /**
     * @param system
     * @param withCluster            启动集群监听
     */
    public AkSystem(ActorSystem system, Boolean withCluster) {
        this.system = system;
        if (withCluster) {
            clusterActor = system.actorOf(Props.create(ClusterActor.class, addressContex));
        }
    }


    /**
     * 生成给
     *
     * @param name
     */
    public void prepareLoadAdd(String name) {
        //地址预加载
        addressContex.prepareLoadAdd(system, name);
    }

    /**
     * actor 注册
     *
     * @param registerBean
     * @return
     */
    public AkSystem register(RegisterBean registerBean) {
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
                addressContex,
                system);
    }


    /**
     * 使用该功能发消息必须要先启动集群监听@AkSystem
     * <p>
     * (ask模式)
     *
     * @param name
     * @param askHandle
     * @return
     */
    public AbstractSenderWrapper createAskMsgWrapper(final String name, AskHandle<?, ?> askHandle) {
        return new AskSenderWrapper<>(
                name,
                addressContex,
                system,
                askHandle);
    }


    public ActorSystem getSystem() {
        return system;
    }

}
