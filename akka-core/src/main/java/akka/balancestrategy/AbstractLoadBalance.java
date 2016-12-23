package akka.balancestrategy;

import akka.actor.ActorRef;
import akka.actor.Address;

import java.util.List;
import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 2016/12/22.
 */
public abstract class AbstractLoadBalance implements LoadBalance {



    /**
     * @needListen true 实现该接口
     * @return
     */
    protected abstract ActorRef needListenStrategy();

    /**
     * @needListen false 实现该接口
     * @return
     */
    protected abstract ActorRef notNeedListenStrategy(Map<Address,ActorRef> actorRefs);

    @Override
    public ActorRef router(Map<Address,ActorRef> actorRefs) {
        if(!needListen()){
            return notNeedListenStrategy(actorRefs);
        }else{
            return needListenStrategy();
        }
    }
}
