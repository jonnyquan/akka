package akka.cluster.addrs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.ClusterContext;
import akka.cluster.routing.ClusterRouterGroup;
import akka.cluster.routing.ClusterRouterGroupSettings;
import akka.enums.RouterGroup;
import akka.routing.Group;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 * 该方法为akka 自带路由功能
 * 缺点:1、ask模式 只能接收到广播的第一条信息
 */
public class RouteesAddress implements ClusterContext {

    private final int MAX_THREAD_COUNT = 100;
    private final ActorSystem system;
    /**
     * 路由地址
     * k path : v getter
     */
    private Map<String, Map<RouterGroup,ActorRef>> routActor;

    public RouteesAddress(ActorSystem system) {
        this.system = system;
        this.routActor = new HashMap<>();
    }

    private void addRoutAdd(String path, RouterGroup routerGroup, ActorRef actorRef) {
        Map<RouterGroup,ActorRef> map = this.routActor.get(path);
        if(!map.containsKey(routerGroup)){
            map.put(routerGroup,actorRef);
        }
    }

    @Override
    public void initReceiversAndBalance(String group,String path, RouterGroup routerGroup) {
        Map<RouterGroup,ActorRef> map = this.routActor.get(path);
        ActorRef actorRef;
        if(map == null){
            map = new HashMap<>();
            this.routActor.put(path,map);
            actorRef = null;
        }else{
           actorRef = map.get(routerGroup);
        }
        if (actorRef == null) {
            Iterable routeesPaths = Arrays.asList(String.format("/user/%s", path));
            Group local = routerGroup.getGroup(routeesPaths);

            ClusterRouterGroup clusterRouterGroup = new ClusterRouterGroup(local,
                    new ClusterRouterGroupSettings(MAX_THREAD_COUNT, routeesPaths,
                            true, group));
            actorRef = system.actorOf(clusterRouterGroup.props());
            addRoutAdd(path, routerGroup, actorRef);
        }
    }

    @Override
    public ActorRef getReceiver(String path, RouterGroup routerGroup) {
        return this.routActor.get(path).get(routerGroup);
    }


}
