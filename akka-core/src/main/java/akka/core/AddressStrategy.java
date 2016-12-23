package akka.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.balancestrategy.LoadBalanceStrategy;
import akka.balancestrategy.ServerStatus;
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

    private  ClusterAddress clusterAddress;

    private ServerStatus serverStatus;


    public AddressStrategy(ClusterAddress clusterAddress, ServerStatus serverStatus) {
        this.clusterAddress = clusterAddress;
        this.serverStatus = serverStatus;
    }

    public  Map<Address,ActorRef> prepareLoadAdd(String path, RouterGroup routerGroup) {
        //集群地址列表预加载
       return clusterAddress.initReceivers(path,null);
    }


    /**
     * 路由模式下广播 ask回调不支持
     * @param name
     * @return
     */
    public  Map<Address,ActorRef> getReceivers(String name) {
       return clusterAddress.getReceivers(name);
    }

    public void onAddressSubcribe(LoadBalanceStrategy loadBalanceStrategy) {
        clusterAddress.addSubcribe(loadBalanceStrategy);
    }

    public void onServerSubcribe(LoadBalanceStrategy loadBalanceStrategy) {
        serverStatus.onServerSubcribe(loadBalanceStrategy);
    }
}
