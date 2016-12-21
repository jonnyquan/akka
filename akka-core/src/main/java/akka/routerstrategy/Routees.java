package akka.routerstrategy;

import akka.actor.ActorRef;
import akka.cluster.routing.ClusterRouterGroup;
import akka.cluster.routing.ClusterRouterGroupSettings;
import akka.msg.Constant;
import akka.routing.Group;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 */
public class Routees {

    private final int MAX_THREAD_COUNT = 100;

    /**
     * 路由地址
     * k path : v getter
     */
    private Map<String, ActorRef> routActor = new HashMap<>();


    /**
     * 路由策略 默认随机 可修改
     */
    private akka.enums.RouterStrategy routerStrategy = akka.enums.RouterStrategy.RANDOM;


    private void addRoutAdd(String path, ActorRef actorRef) {
        routActor.put(path, actorRef);
    }

    public ActorRef existActor(String path) {
        ActorRef actorRef = this.routActor.get(path);
        if (actorRef == null) {
            actorRef = getRouterActor( Arrays.asList(String.format("/user/%s", path)));
            addRoutAdd(path, actorRef);
        }
        return actorRef;
    }


    private ActorRef getRouterActor( Iterable<String> routeesPaths) {

        Group local = routerStrategy.getGroup(routeesPaths);

        ClusterRouterGroup clusterRouterGroup = new ClusterRouterGroup(local,
                new ClusterRouterGroupSettings(MAX_THREAD_COUNT, routeesPaths,
                        false, Constant.ROLE_NAME));
        return system.actorOf(clusterRouterGroup.props());
    }

}
