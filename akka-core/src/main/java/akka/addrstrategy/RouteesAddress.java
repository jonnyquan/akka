package akka.addrstrategy;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.routing.ClusterRouterGroup;
import akka.cluster.routing.ClusterRouterGroupSettings;
import akka.enums.RouterGroup;
import akka.msg.Constant;
import akka.routing.Group;
import akka.routing.RoundRobinGroup;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 */
public class RouteesAddress implements AddressContext{

    private final int MAX_THREAD_COUNT = 100;

    /**
     * 路由地址
     * k path : v getter
     */
    private Map<String, ActorRef> routActor = new HashMap<>();



    private void addRoutAdd(String path, ActorRef actorRef) {
        routActor.put(path, actorRef);
    }

    @Override
    public void initReceivers(ActorSystem system, String path, RouterGroup routerGroup) {
        ActorRef actorRef = this.routActor.get(path);
        if (actorRef == null) {
            Iterable routeesPaths = Arrays.asList(String.format("/user/%s", path));
            Group local = routerGroup.getGroup(routeesPaths);

            ClusterRouterGroup clusterRouterGroup = new ClusterRouterGroup(local,
                    new ClusterRouterGroupSettings(MAX_THREAD_COUNT, routeesPaths,
                            true, Constant.ROLE_NAME));
            actorRef = system.actorOf(clusterRouterGroup.props());
            addRoutAdd(path, actorRef);
        }
    }


    public List<ActorRef> getReceivers(String path){
        return Arrays.asList(this.routActor.get(path));
    }



}
