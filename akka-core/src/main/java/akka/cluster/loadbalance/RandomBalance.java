package akka.cluster.loadbalance;


import akka.actor.Address;
import akka.cluster.metrics.NodeMetrics;
import akka.enums.RouterGroup;

import java.util.Random;
import java.util.Set;
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
    public void updateAddr(Set<Address> actorRefMap) {

    }

    @Override
    public void updateServerStatu(Iterable<NodeMetrics> nodeMetrics) {

    }


    @Override
    protected Address needListenStrategy() {
        return null;
    }

    @Override
    protected Address notNeedListenStrategy(Set<Address> actorRefMap) {
        int randomNum = random.nextInt(actorRefMap.size());
        return actorRefMap.stream().collect(Collectors.toList()).get(randomNum);
    }
}
