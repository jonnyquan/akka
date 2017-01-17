package akka.core;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actors.BaseService;
import akka.cluster.routing.ClusterRouterGroup;
import akka.enums.RouterGroup;
import akka.params.AskResponseResolver;
import akka.params.DefaultAskResponseResolver;
import akka.params.RegisterWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 提供顶级接口的实现
 * <p>
 * Created by ruancl@xkeshi.com on 16/10/9.
 */
public class AkkaSystemContext extends AbstractAkkaSystemContext {

    private static final Logger logger = LoggerFactory.getLogger(AkkaSystemContext.class);

    private final Long DEFAULT_ASK_TIME_OUT = 20000L;

    private final AskResponseResolver defaultAskHandler = new DefaultAskResponseResolver();

    private final RouterGroup defaultRouter = RouterGroup.RANDOM;

    /**
     * @param systemName
     */
    public AkkaSystemContext(String systemName) {
        super(systemName);
    }

    /**
     * actor 注册
     *
     * @param registerWrapper
     * @return
     */
    @Override
    public Akka register(RegisterWrapper registerWrapper) {
        registerActor(registerWrapper);
        return this;
    }

    @Override
    public ActorRef registerActor(RegisterWrapper registerWrapper) {
        ActorRef ref;
        BaseService proxy = registerWrapper.getRef();
            ref = getSystem().actorOf(registerWrapper.getPool().props(Props.create(registerWrapper.gettClass(), proxy)), registerWrapper.getName());
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
    private TellSender createTellMsgWrapper(final String group,final String name, final RouterGroup routerGroup) {
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
     * @param askResponseResolver
     * @return
     */
    private AskSender createAskMsgWrapper(final String group, final String name, AskResponseResolver askResponseResolver, final RouterGroup routerGroup, long timeOut) {
        return new AskSenderWrapper(
                group,
                name,
                askResponseResolver,
                routerGroup,
                timeOut,
                this);
    }




    @Override
    public TellSender createTellSender(String group,String name, RouterGroup routerGroup) {
        return createTellMsgWrapper(group,name, routerGroup);
    }

    @Override
    public AskSender createAskSender(String group,String name) {
        return createAskSender(group,name, defaultAskHandler, defaultRouter,DEFAULT_ASK_TIME_OUT);
    }

    @Override
    public TellSender createTellSender(String group,String name) {
        return createTellSender(group,name, defaultRouter);
    }

    @Override
    public AskSender createAskSender(String group, String name, AskResponseResolver askResponseResolver, RouterGroup routerGroup, long timeOut) {
        return createAskMsgWrapper(group,name, askResponseResolver,routerGroup,timeOut);
    }


}
