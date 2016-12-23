package akka.balancestrategy;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.cluster.metrics.NodeMetrics;
import akka.enums.RouterGroup;

import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 2016/12/22.
 */
public interface LoadBalance {

    boolean matchRouterGroup(RouterGroup routerGroup);

    /**
     *
     * @return ture 需要监听地址(必须实现update)  false不监听
     */
   boolean needListenAddr();

    /**
     * 监听服务器状态
     * @return
     */
    boolean needListenStatus();

    /**
     * needListen true需要实现
     * @param map
     */
    void updateAddr(Map<Address,ActorRef> map);

    void updateServerStatu(Iterable<NodeMetrics> nodeMetrics);

    /**
     *
     * @param actorRefs 根据策略可以传null
     * @return
     */
    ActorRef router(Map<Address,ActorRef> actorRefs);
}
