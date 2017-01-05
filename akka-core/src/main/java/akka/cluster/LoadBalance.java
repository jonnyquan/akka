package akka.cluster;

import akka.actor.Address;
import akka.cluster.metrics.NodeMetrics;
import akka.enums.RouterGroup;

import java.util.Set;

/**
 * Created by ruancl@xkeshi.com on 2016/12/22.
 * 负载均衡(路由策略的实现)
 */
public interface LoadBalance {

    boolean matchRouterGroup(RouterGroup routerGroup);

    /**
     * @return ture 需要监听地址(必须实现update)  false不监听
     */
    boolean needListenAddr();

    /**
     * 监听服务器状态
     *
     * @return
     */
    boolean needListenStatus();

    /**
     * needListenAddr true需要实现  集群地址变更通知
     *
     * @param actorRefMap
     */
    void updateAddr(Set<Address> actorRefMap);

    /**
     * needListenStatus true   服务器状态变更通知
     *
     * @param nodeMetrics
     */
    void updateServerStatu(Iterable<NodeMetrics> nodeMetrics);

    /**
     * @param actorRefMap 根据策略可以传null
     * @return
     */
    Address router(Set<Address> actorRefMap);
}
