package akka.enums;

import akka.routing.*;

/**
 * Created by ruancl@xkeshi.com on 16/10/20.
 * 路由模式的路由策略
 * Pool表示消息接收方 执行方的actor(线程)策略
 * Group表示消息发送时候  选择路由的模式
 */
public enum RouterPool {


    ROBIN {
        @Override
        public Pool getPool(int num) {
            return new RoundRobinPool(num);
        }
    }, RANDOM {
        @Override
        public Pool getPool(int num) {
            return new RandomPool(num);
        }
    }, BALANCE {
        @Override
        public Pool getPool(int num) {
            return new BalancingPool(num);
        }
    }, CONSISTENTHASH {
        @Override
        public Pool getPool(int num) {
            return new ConsistentHashingPool(num);
        }
    };

    public abstract Pool getPool(int num);
}
