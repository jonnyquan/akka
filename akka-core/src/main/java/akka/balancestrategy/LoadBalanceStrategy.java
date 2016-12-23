package akka.balancestrategy;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.enums.RouterGroup;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 2016/12/22.
 */
public class LoadBalanceStrategy {

    private RandomBalance random;

    private RoundRobinBalance roundRobin;

    private AdaptiveBalance adaptiveBalance;

    private List<LoadBalance> loadBalances;

    public LoadBalanceStrategy(Map<Address, ActorRef> actorRefMap) {
        this.random = new RandomBalance();
        this.roundRobin = new RoundRobinBalance(actorRefMap);
        this.adaptiveBalance = new AdaptiveBalance();
        this.loadBalances = Arrays.asList(random,roundRobin,adaptiveBalance);
    }

    public List<LoadBalance> getLoadBalances() {
        return loadBalances;
    }



    public ActorRef router(Map<Address,ActorRef> actors, RouterGroup routerGroup){
        return loadBalances.stream().filter(o->o.matchRouterGroup(routerGroup)).findFirst().get().router(actors);
    }
}
