package akka.balancestrategy;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.enums.RouterGroup;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 2016/12/23.
 */
public class RoundRobinBalance extends AbstractLoadBalance {


    private LinkedList<ActorRef> actorRefs;

    public RoundRobinBalance(Map<Address,ActorRef> map) {
        this.actorRefs = new LinkedList<>();
        map.values().forEach(o->actorRefs.push(o));
    }

    @Override
    public boolean matchRouterGroup(RouterGroup routerGroup) {
        return routerGroup == RouterGroup.ROBIN;
    }

    @Override
    public boolean needListen() {
        return true;
    }

    @Override
    public void update(Map<Address, ActorRef> map) {
        synchronized (actorRefs){
            actorRefs.clear();
            map.values().forEach(o->actorRefs.push(o));
        }
    }

    @Override
    protected ActorRef needListenStrategy() {
        ActorRef back;
        synchronized (actorRefs) {
            back = actorRefs.pollLast();
            actorRefs.push(back);
        }
        return back;
    }

    @Override
    protected ActorRef notNeedListenStrategy(Map<Address,ActorRef> actorRefs) {
        return null;
    }
}
