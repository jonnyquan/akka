package akka.cluster.loadbalance;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.cluster.metrics.NodeMetrics;
import akka.enums.RouterGroup;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 2016/12/23.
 */
public class RoundRobinBalance extends AbstractLoadBalance {


    private LinkedList<ActorRef> actorRefs = new LinkedList<>();


    @Override
    public boolean matchRouterGroup(RouterGroup routerGroup) {
        return routerGroup == RouterGroup.ROBIN;
    }

    @Override
    public boolean needListenAddr() {
        return true;
    }

    @Override
    public boolean needListenStatus() {
        return false;
    }

    @Override
    public void updateAddr(Map<Address, ActorRef> map) {
        synchronized (actorRefs) {
            actorRefs.clear();
            map.values().forEach(o -> actorRefs.push(o));
        }
    }

    @Override
    public void updateServerStatu(Iterable<NodeMetrics> nodeMetrics) {

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
    protected ActorRef notNeedListenStrategy(Map<Address, ActorRef> actorRefs) {
        return null;
    }
}
