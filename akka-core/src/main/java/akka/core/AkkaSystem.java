package akka.core;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.cluster.routing.ClusterRouterGroup;
import akka.enums.RouterGroup;
import akka.params.AskProcessHandler;
import akka.params.DefaultAskProcessHandler;
import akka.params.RegisterBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供顶级接口的实现
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
        registerActor(registerBean);
        return this;
    }

    @Override
    public ActorRef registerActor(RegisterBean registerBean) {
        ActorRef ref;
        Object[] params = registerBean.getParams();
        if (params != null) {
            ref = getSystem().actorOf(registerBean.getPool().props(Props.create(registerBean.gettClass(), registerBean.getParams())), registerBean.getName());
        } else {
            ref = getSystem().actorOf(registerBean.getPool().props(Props.create(registerBean.gettClass())), registerBean.getName());
        }

        logger.info("register actor:{}", ref.path());
        System.out.println("注册actor:" + ref.path() + "成功========================");
        return ref;
    }

    @Override
    public ActorRef registerRouterActor(ClusterRouterGroup clusterRouterGroup) {
        return getSystem().actorOf(clusterRouterGroup.props());
    }


    /**
     * (tell模式)
     *
     * @param name
     * @return
     */
    private Sender createTellMsgWrapper(final String group,final String name, final RouterGroup routerGroup) {
        return new TellSenderWrapper(
                group,
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
    private Sender createAskMsgWrapper(final String group,final String name, AskProcessHandler<?, ?> askProcessHandler, final RouterGroup routerGroup) {
        return new AskSenderWrapper<>(
                group,
                name,
                askProcessHandler,
                routerGroup,
                this);
    }


    @Override
    public Sender createAskSender( String group,String name, RouterGroup routerGroup) {
        return createAskMsgWrapper(group,name, new DefaultAskProcessHandler(), routerGroup);
    }

    @Override
    public Sender createAskSender(String group,String name, AskProcessHandler<?, ?> askProcessHandler, RouterGroup routerGroup) {
        return createAskMsgWrapper(group,name, askProcessHandler, routerGroup);
    }

    @Override
    public Sender createTellSender(String group,String name, RouterGroup routerGroup) {
        return createTellMsgWrapper(group,name, routerGroup);
    }

    @Override
    public Sender createAskSender(String group,String name) {
        return createAskSender(group,name, new DefaultAskProcessHandler(), RouterGroup.RANDOM);
    }

    @Override
    public Sender createTellSender(String group,String name) {
        return createTellSender(group,name, RouterGroup.RANDOM);
    }


}
