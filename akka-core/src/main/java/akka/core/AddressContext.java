package akka.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.Props;
import akka.actors.DefaultSenderActor;
import akka.cluster.metrics.AdaptiveLoadBalancingGroup;
import akka.cluster.metrics.HeapMetricsSelector;
import akka.cluster.routing.ClusterRouterGroup;
import akka.cluster.routing.ClusterRouterGroupSettings;
import akka.enums.RouterStrategy;
import akka.msg.Constant;
import akka.routing.Group;
import akka.routing.RandomGroup;
import akka.routing.RoundRobinGroup;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 16/11/9.
 *
 * 维护了路由模式的actor引用和广播模式的actor引用
 *
 */
public class AddressContext {


    private final int MAX_THREAD_COUNT = 100;

    /**
     * 集群中所有存活的地址
     */
    private List<Address> addresses = new ArrayList<>();

    /**
     * 路由策略 默认随机 可修改
     */
    private RouterStrategy routerStrategy = RouterStrategy.RANDOM;

    /**
     * 集群的actor地址维护  k: actorName  v:集群中actor引用
     */
    private Map<String, List<ActorRefMap>> map = new HashMap<>();

    /**
     * 集群模式下消息初始化actor 用作初始化接收方actorRef   key为actorName 不重复生成
     */
    private Map<String, ActorRef> sender = new HashMap<>();

    /**
     * 路由地址
     * k path : v getter
     */
    private Map<String, ActorRef> routActor = new HashMap<>();

    public ActorRef getSender(ActorSystem system, String path) {
        ActorRef senderActor = sender.get(path);
        if (senderActor == null) {
            senderActor = system.actorOf(Props.create(DefaultSenderActor.class, this, path));
            sender.put(path, senderActor);
        }
        return senderActor;
    }

    public void prepareLoadAdd(ActorSystem system, String path) {
        //路由地址预加载
        getRoutActor(system, path);
        //集群地址列表预加载
        getSender(system, path);
    }


    private void addRoutAdd(String path, ActorRef actorRef) {
        routActor.put(path, actorRef);
    }

    public ActorRef getRoutActor(ActorSystem system, String path) {
        ActorRef actorRef = this.routActor.get(path);
        if (actorRef == null) {
            actorRef = getRouterActor(system, Arrays.asList(String.format("/user/%s", path)));
            addRoutAdd(path, actorRef);
        }
        return actorRef;
    }


    private ActorRef getRouterActor(ActorSystem system, Iterable<String> routeesPaths) {

        Group local = routerStrategy.getGroup(routeesPaths);

        ClusterRouterGroup clusterRouterGroup = new ClusterRouterGroup(local,
                new ClusterRouterGroupSettings(MAX_THREAD_COUNT, routeesPaths,
                        false, Constant.ROLE_NAME));

        return system.actorOf(clusterRouterGroup.props());
    }



    public void addAddress(Address address) {
        synchronized (addresses) {
            addresses.add(address);
        }
    }

    /**
     * 移除断线的机器
     * @param address
     */
    public void deleteAddress(Address address) {
        synchronized (addresses) {
            addresses.remove(address);
            deleteActorRef(address);
        }
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    private void deleteActorRef(Address address) {
        synchronized (map) {
            for (String key : map.keySet()) {
                List<ActorRefMap> actorRefMaps = map.get(key);
                map.put(key, actorRefMaps.stream().filter(o -> !o.containAddr(address)).collect(Collectors.toList()));
            }
        }
    }

    public List<ActorRefMap> getActorRefs(String key) {
        return map.get(key);
    }


    public synchronized void addMap(String key, List<ActorRefMap> actorRefMaps) {
        if (map.containsKey(key)) {
            throw new IllegalArgumentException("请勿重复生成消息任务");
        }
        synchronized (map) {
            map.put(key, actorRefMaps);
        }
    }
}
