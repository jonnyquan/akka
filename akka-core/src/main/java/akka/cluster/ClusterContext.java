package akka.cluster;

import akka.actor.ActorRef;
import akka.enums.RouterGroup;


/**
 * Created by ruancl@xkeshi.com on 2016/12/27.
 * 集群 维护集群的地址 服务器状态
 * 每个sender 初始化的时候会来调用@initReceiversAndBalance
 * 发送消息时候调用@getReceivers
 */
public interface ClusterContext {


    /**
     * 初始化接受端 和负载均衡器
     *
     * @param gettersKey
     * @param routerGroup
     */
    void initReceiversAndBalance(String group,String gettersKey, RouterGroup routerGroup);

    /**
     * 获取接收方actorRef
     *
     * @param gettersKey
     * @param routerGroup
     * @return
     */
    ActorRef getReceiver(String gettersKey, RouterGroup routerGroup);


}
