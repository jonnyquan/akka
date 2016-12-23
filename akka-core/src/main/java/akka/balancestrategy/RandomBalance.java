package akka.balancestrategy;


import akka.actor.ActorRef;
import akka.actor.Address;
import akka.cluster.metrics.NodeMetrics;
import akka.enums.RouterGroup;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 2016/12/22.
 */
public class RandomBalance extends AbstractLoadBalance {

    private Random random = new Random();
    @Override
    public boolean matchRouterGroup(RouterGroup routerGroup) {
        return routerGroup == RouterGroup.RANDOM;
    }

    @Override
    public boolean needListenAddr() {
        return false;
    }

    @Override
    public boolean needListenStatus() {
        return false;
    }

    @Override
    public void updateAddr(Map<Address, ActorRef> map) {

    }

    @Override
    public void updateServerStatu(Iterable<NodeMetrics> nodeMetrics) {

    }


    @Override
    protected ActorRef needListenStrategy() {
        return null;
    }

    @Override
    protected ActorRef notNeedListenStrategy(Map<Address,ActorRef> actorRefs) {
        int randomNum = random.nextInt(actorRefs.size());
        List<ActorRef> list = actorRefs.values().stream().collect(Collectors.toList());
        return list.get(randomNum);
    }
}
