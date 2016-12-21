package akka.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.enums.RouterGroup;
import akka.addrstrategy.ClusterAddress;
import akka.addrstrategy.RouteesAddress;

import java.util.*;

/**
 * Created by ruancl@xkeshi.com on 16/11/9.
 *
 *  策略包装类  actor引用 路由方式的选择
 *
 */
public class AddressStrategy {

    private static ClusterAddress clusterAddress = new ClusterAddress();

    private static RouteesAddress routees = new RouteesAddress();


    public static void prepareLoadAdd(ActorSystem system,String path, RouterGroup routerGroup) {
        //路由地址预加载
        routees.initReceivers(system,path, routerGroup);
        //集群地址列表预加载
        clusterAddress.initReceivers(system,path,null);
    }


    /**
     * 路由模式下广播 ask回调不支持
     * @param name
     * @param routerGroup
     * @return
     */
    public static List<ActorRef> getReceivers(String name,RouterGroup routerGroup) {
        if (routerGroup != RouterGroup.BROADCAST) {
            return routees.getReceivers(name);
        }
       return clusterAddress.getReceivers(name);
    }

    public static ClusterAddress getClusterAddress() {
        return clusterAddress;
    }
}
