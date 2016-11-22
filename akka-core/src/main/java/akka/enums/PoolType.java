package akka.enums;

import akka.routing.*;

/**
 * Created by ruancl@xkeshi.com on 16/10/20.
 */
public enum PoolType {

    ROUBIN, RANDOM, BROADCAST, BALANCE, CONSISTENTHASH;


    public Pool getPool(int num) {
        switch (this) {
            case ROUBIN:
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
