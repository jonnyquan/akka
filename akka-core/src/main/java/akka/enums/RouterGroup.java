package akka.enums;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.cluster.LoadBalance;
import akka.cluster.loadbalance.AdaptiveBalance;
import akka.cluster.loadbalance.RandomBalance;
import akka.cluster.loadbalance.RoundRobinBalance;
import akka.cluster.metrics.AdaptiveLoadBalancingGroup;
import akka.cluster.metrics.HeapMetricsSelector;
import akka.routing.*;

import java.util.Collections;
import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 16/10/20.
 * 路由模式的路由策略
 * Pool表示消息接收方 执行方的actor(线程)策略
 * Group表示消息发送时候  选择路由的模式
 */
public enum RouterGroup {


    ROBIN{
        @Override
        public Group getGroup(Iterable<String> routeesPaths) {
            return new RandomGroup(routeesPaths);
        }

        @Override
        public LoadBalance createAndSetLoadBalance() {
            return new RoundRobinBalance();
        }
    }, RANDOM{
        @Override
        public Group getGroup(Iterable<String> routeesPaths) {
            return new RoundRobinGroup(routeesPaths);
        }

        @Override
        public LoadBalance createAndSetLoadBalance() {
            return new RandomBalance();
        }
    }, BROADCAST{
        @Override
        public Group getGroup(Iterable<String> routeesPaths) {
            return new BroadcastGroup(routeesPaths);
        }

        @Override
        public LoadBalance createAndSetLoadBalance() {
            return null;
        }
    }, BALANCE{
        @Override
        public Group getGroup(Iterable<String> routeesPaths) {
            return new AdaptiveLoadBalancingGroup(HeapMetricsSelector.getInstance(),
                    Collections.emptyList());
        }

        @Override
        public LoadBalance createAndSetLoadBalance() {
            return new AdaptiveBalance();
        }
    }
    /*, CONSISTENTHASH{
        @Override
        public Group getGroup(Iterable<String> routeesPaths) {
            return new ConsistentHashingGroup(routeesPaths);
        }
    }*/;


    public abstract Group getGroup(Iterable<String> routeesPaths);

    public abstract LoadBalance createAndSetLoadBalance();
}
