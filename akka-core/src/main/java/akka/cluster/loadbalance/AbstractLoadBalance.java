package akka.cluster.loadbalance;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.cluster.LoadBalance;

import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 2016/12/22.
 */
public abstract class AbstractLoadBalance implements LoadBalance {


    /**
     * @return
     * @needListen true 实现该接口
     */
    protected abstract ActorRef needListenStrategy();

    /**
     * @return
     * @needListen false 实现该接口
     */
    protected abstract ActorRef notNeedListenStrategy(Map<Address, ActorRef> actorRefs);

    @Override
    public ActorRef router(Map<Address, ActorRef> actorRefs) {
        if (!needListenAddr()) {
            return notNeedListenStrategy(actorRefs);
        } else {
            return needListenStrategy();
        }
    }
}
