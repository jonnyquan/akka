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

    private  Optional<ClusterAddress> clusterAddress;

    private  Optional<RouteesAddress> routees;


    public AddressStrategy(Optional<ClusterAddress> clusterAddress, Optional<RouteesAddress> routees) {
        this.clusterAddress = clusterAddress;
        this.routees = routees;
    }

    public  void prepareLoadAdd(String path, RouterGroup routerGroup) {
        //路由地址预加载
        routees.ifPresent(o->o.initReceivers(path, routerGroup));
        //集群地址列表预加载
        clusterAddress.ifPresent(o->o.initReceivers(path,null));
    }


    /**
     * 路由模式下广播 ask回调不支持
     * @param name
     * @param routerGroup
     * @return
     */
    public  List<ActorRef> getReceivers(String name,RouterGroup routerGroup) {
        if (routerGroup != RouterGroup.BROADCAST && routees.isPresent()) {
            return routees.get().getReceivers(name,routerGroup);
        }
       return clusterAddress.orElseThrow(NullPointerException::new).getReceivers(name,routerGroup);
    }

}
