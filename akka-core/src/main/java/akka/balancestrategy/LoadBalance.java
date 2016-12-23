package akka.balancestrategy;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.enums.RouterGroup;

import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 2016/12/22.
 */
public interface LoadBalance {

    boolean matchRouterGroup(RouterGroup routerGroup);

    /**
     *
     * @return ture 需要监听(必须实现update)  false不监听
     */
   boolean needListen();

    /**
     * needListen true需要实现
     * @param map
     */
    void update(Map<Address,ActorRef> map);

    /**
     *
     * @param actorRefs 根据策略可以传null
     * @return
     */
    ActorRef router(Map<Address,ActorRef> actorRefs);
}
