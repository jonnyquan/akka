package akka.enter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.Props;
import akka.actors.DefaultSenderActor;
import akka.cluster.metrics.AdaptiveLoadBalancingGroup;
import akka.cluster.metrics.HeapMetricsSelector;
import akka.cluster.routing.ClusterRouterGroup;
import akka.cluster.routing.ClusterRouterGroupSettings;
import akka.msg.Constant;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 16/11/9.
 */
public class AddressContex {


    private final int MAX_THREAD_COUNT = 100;

    private List<Address> addresses = new ArrayList<>();

    /**
     * 集群的地址维护
     */
    private Map<String, List<ActorRefMap>> map = new HashMap<>();

    /**
     * 路由地址
     * k path : v getter
     */
    private Map<String, ActorRef> routActor = new HashMap<>();

    /**
     * 发送器actor
     */
    private Map<String, ActorRef> sender = new HashMap<>();

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
        ClusterRouterGroup clusterRouterGroup = new ClusterRouterGroup(
                new AdaptiveLoadBalancingGroup(
                        HeapMetricsSelector.getInstance(),
                        Collections.emptyList()),
                new ClusterRouterGroupSettings(MAX_THREAD_COUNT, routeesPaths,
                        false, Constant.ROLE_NAME));
        return system.actorOf(clusterRouterGroup.props());
    }


    public void addAddress(Address address) {
        synchronized (addresses) {
            addresses.add(address);
        }
    }

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
                map.put(key, actorRefMaps.stream().filter(o -> !o.removeAdd(address)).collect(Collectors.toList()));
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
