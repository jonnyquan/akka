package akka.cluster.loadbalance;

import akka.actor.Address;
import akka.cluster.metrics.NodeMetrics;
import akka.enums.RouterGroup;

import java.util.LinkedList;
import java.util.Set;

/**
 * Created by ruancl@xkeshi.com on 2016/12/23.
 */
public class RoundRobinBalance extends AbstractLoadBalance {


    private LinkedList<Address> actorRefs = new LinkedList<>();


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
    public void updateAddr(Set<Address> actorRefMap) {
        synchronized (actorRefs) {
            actorRefs.clear();
            actorRefMap.forEach(o -> actorRefs.push(o));
        }
    }

    @Override
    public void updateServerStatu(Iterable<NodeMetrics> nodeMetrics) {

    }

    @Override
    protected Address needListenStrategy() {
        Address back;
        synchronized (actorRefs) {
            back = actorRefs.pollLast();
            actorRefs.push(back);
        }
        return back;
    }

    @Override
    protected Address notNeedListenStrategy(Set<Address> actorRefMap) {
        return null;
    }
}
