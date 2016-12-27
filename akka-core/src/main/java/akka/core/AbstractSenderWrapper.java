package akka.core;

import akka.actor.ActorRef;
import akka.cluster.ClusterInterface;
import akka.enums.RouterGroup;
import akka.msg.Message;

import java.util.List;

/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 * 抽象(集群 与路由)发消息类
 */
public abstract class AbstractSenderWrapper implements Sender {

    private final String gettersKey;
    private ClusterInterface clusterInterface;
    private RouterGroup routerGroup;


    protected AbstractSenderWrapper(String gettersKey, RouterGroup routerGroup, AbstractAkkaSystem akkaSystem) {
        this.clusterInterface = akkaSystem.getClusterInterface();
        this.routerGroup = routerGroup;
        this.gettersKey = gettersKey;
        // TODO: 2016/12/27 loadBanlance放枚举类里面处理是错误的 
        if (clusterInterface.useIdentifyLoadBalance()) {
            routerGroup.createAndSetLoadBalance();
        }
        this.clusterInterface.initReceiversAndBalance(gettersKey, routerGroup);
    }


    /**
     * 每次消息发送 都会去addressContext获取相应的接收方 actorRef
     *
     * @return
     */
    protected List<ActorRef> getGetters() {
        return clusterInterface.getReceivers(this.gettersKey, routerGroup);
    }


    /**
     * @param message
     * @return
     */
    @Override
    public Object sendMsg(Message message) {
        return handleMsg(message);
    }


    protected abstract Object handleMsg(Message message);
}
