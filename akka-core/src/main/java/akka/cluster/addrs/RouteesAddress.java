package akka.cluster.addrs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.ClusterInterface;
import akka.cluster.routing.ClusterRouterGroup;
import akka.cluster.routing.ClusterRouterGroupSettings;
import akka.enums.RouterGroup;
import akka.msg.Constant;
import akka.routing.Group;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 * 该方法为akka 自带路由功能
 * 缺点:1、ask模式 只能接收到广播的第一条信息
 * 2、他无法识别某一台机器是否存在所需要的actor  无差别发送
 */
public class RouteesAddress implements ClusterInterface {

    private static final String PATCH = "//$";
    private final int MAX_THREAD_COUNT = 100;
    private final ActorSystem system;
    /**
     * 路由地址
     * k path : v getter
     */
    private Map<String, ActorRef> routActor;

    public RouteesAddress(ActorSystem system) {
        this.system = system;
        this.routActor = new HashMap<>();
    }

    private void addRoutAdd(String path, RouterGroup routerGroup, ActorRef actorRef) {
        routActor.put(String.format("%s%s%s", path, PATCH, routerGroup.toString()), actorRef);
    }

    @Override
    public void initReceiversAndBalance(String path, RouterGroup routerGroup) {
        ActorRef actorRef = this.routActor.get(path);
        if (actorRef == null) {
            Iterable routeesPaths = Arrays.asList(String.format("/user/%s", path));
            Group local = routerGroup.getGroup(routeesPaths);

            ClusterRouterGroup clusterRouterGroup = new ClusterRouterGroup(local,
                    new ClusterRouterGroupSettings(MAX_THREAD_COUNT, routeesPaths,
                            true, Constant.ROLE_NAME));
            actorRef = system.actorOf(clusterRouterGroup.props());
            addRoutAdd(path, routerGroup, actorRef);
        }
    }

    @Override
    public List<ActorRef> getReceivers(String path, RouterGroup routerGroup) {
        return Arrays.asList(this.routActor.get(String.format("%s%s%s", path, PATCH, routerGroup.toString())));
    }


    @Override
    public Boolean useIdentifyLoadBalance() {
        return false;
    }


}
