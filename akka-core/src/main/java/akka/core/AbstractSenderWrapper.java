package akka.core;

import akka.actor.ActorRef;
import akka.cluster.ClusterContext;
import akka.enums.RouterGroup;
import akka.msg.Message;

import java.util.List;

/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 * 抽象(集群 与路由)发消息类
 */
public abstract class AbstractSenderWrapper implements Sender {

    private final String gettersKey;
    private ClusterContext clusterContext;
    private RouterGroup routerGroup;
    private final String groupName;

    @Override
    public String getGroupName() {
        return groupName;
    }

    @Override
    public String getTopicName() {
        return gettersKey;
    }
    @Override
    public RouterGroup getRouterGroup() {
        return routerGroup;
    }

    protected AbstractSenderWrapper(String group, String gettersKey, RouterGroup routerGroup, AbstractAkkaSystem akkaSystem) {
        this.groupName = group;
        this.clusterContext = akkaSystem.getClusterContext();
        this.routerGroup = routerGroup;
        this.gettersKey = gettersKey;
        this.clusterContext.initReceiversAndBalance(group,gettersKey, routerGroup);
    }


    /**
     * 每次消息发送 都会去addressContext获取相应的接收方 actorRef
     *
     * @return
     */
    protected List<ActorRef> getGetters() {
        return clusterContext.getReceivers(this.gettersKey, routerGroup);
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
