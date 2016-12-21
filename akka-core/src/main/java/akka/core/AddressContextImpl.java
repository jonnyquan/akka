package akka.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.enums.RouterStrategy;
import akka.enums.TransferType;
import akka.routerstrategy.ClusterStrategy;
import akka.routerstrategy.Routees;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 16/11/9.
 *
 * 维护了路由模式的actor引用和广播模式的actor引用
 *
 */
public class AddressContextImpl{



    private ActorSystem system;
    private ClusterStrategy clusterStrategy;
    private Routees routees;


    public AddressContextImpl(ActorSystem actorSystem) {
        this.system = actorSystem;
        this.clusterStrategy = new ClusterStrategy();
        this.routees = new Routees();

    }



    public void prepareLoadAdd( String path) {
        //路由地址预加载
        routees.existActor(this.system,path);
        //集群地址列表预加载
        clusterStrategy.initReceivers(this.system,path);
    }



    public List<ActorRef> getReceivers(TransferType transferType,String name) {
        if (transferType == TransferType.ROUTER) {
            return Arrays.asList(routees.existActor(name));
        }
       return clusterStrategy.getReceivers(name);
    }

}
