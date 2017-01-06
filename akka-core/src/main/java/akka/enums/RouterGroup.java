package akka.enums;

import akka.cluster.metrics.AdaptiveLoadBalancingGroup;
import akka.cluster.metrics.HeapMetricsSelector;
import akka.routing.*;

import java.util.Collections;

/**
 * Created by ruancl@xkeshi.com on 16/10/20.
 * 路由模式的路由策略
 * Pool表示消息接收方 执行方的actor(线程)策略
 * Group表示消息发送时候  选择路由的模式
 */
public enum RouterGroup {


    ROBIN {
        @Override
        public Group getGroup(Iterable<String> routeesPaths) {
            return new RandomGroup(routeesPaths);
        }

    }, RANDOM {
        @Override
        public Group getGroup(Iterable<String> routeesPaths) {
            return new RoundRobinGroup(routeesPaths);
        }

    }, BROADCAST {
        @Override
        public Group getGroup(Iterable<String> routeesPaths) {
            return new BroadcastGroup(routeesPaths);
        }

    }, BALANCE {
        @Override
        public Group getGroup(Iterable<String> routeesPaths) {
            return new AdaptiveLoadBalancingGroup(HeapMetricsSelector.getInstance(),
                    Collections.emptyList());
        }

    }
    /*, CONSISTENTHASH{
        @Override
        public Group getGroup(Iterable<String> routeesPaths) {
            return new ConsistentHashingGroup(routeesPaths);
        }
    }*/;


    public abstract Group getGroup(Iterable<String> routeesPaths);

}
