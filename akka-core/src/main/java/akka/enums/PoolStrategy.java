package akka.enums;

import akka.routing.*;


/**
 * Created by ruancl@xkeshi.com on 16/10/20.
 * 路由模式的路由策略
 * Pool表示消息接收方 执行方的actor(线程)策略
 * Group表示消息发送时候  选择路由的模式
 */
public enum PoolStrategy {


    ROBIN, RANDOM, BROADCAST, BALANCE, CONSISTENTHASH;


    public Pool getPool(int num) {
        switch (this) {
            case ROBIN:
                return new RoundRobinPool(num);
            case RANDOM:
                return new RandomPool(num);
            case BALANCE:
                return new BalancingPool(num);
            case BROADCAST:
                return new BroadcastPool(num);
            case CONSISTENTHASH:
                return new ConsistentHashingPool(num);
        }
        return new RoundRobinPool(1);
    }

}
