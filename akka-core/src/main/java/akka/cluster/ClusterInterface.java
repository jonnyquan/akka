package akka.cluster;

import akka.actor.ActorRef;
import akka.enums.RouterGroup;

import java.util.List;


/**
 * Created by ruancl@xkeshi.com on 2016/12/27.
 * 集群 维护集群的地址 服务器状态
 */
public interface ClusterInterface {


    /**
     * 初始化接受端 和负载均衡器
     *
     * @param gettersKey
     * @param routerGroup
     */
    void initReceiversAndBalance(String gettersKey, RouterGroup routerGroup);

    /**
     * 获取接收方actorRef
     *
     * @param gettersKey
     * @param routerGroup
     * @return
     */
    List<ActorRef> getReceivers(String gettersKey, RouterGroup routerGroup);


}
