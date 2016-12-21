package akka.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.enums.RouterStrategy;
import akka.enums.TransferType;
import akka.addrstrategy.ClusterAddress;
import akka.addrstrategy.RouteesAddress;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ruancl@xkeshi.com on 16/11/9.
 *
 *  策略包装类  actor引用 路由方式的选择
 *
 */
public class AddressStrategy {

    private static ClusterAddress clusterAddress = new ClusterAddress();

    private static RouteesAddress routees = new RouteesAddress();


    public static void prepareLoadAdd(ActorSystem system,String path, RouterStrategy routerStrategy) {
        //路由地址预加载
        routees.existActor(system,path,routerStrategy);
        //集群地址列表预加载
        clusterAddress.initReceivers(system,path);
    }



    public static List<ActorRef> getReceivers(String name,RouterStrategy routerStrategy) {
        if (routerStrategy != RouterStrategy.BROADCAST) {
            return Arrays.asList(routees.getReceiver(name));
        }
       return clusterAddress.getReceivers(name);
    }

    public static ClusterAddress getClusterAddress() {
        return clusterAddress;
    }
}
