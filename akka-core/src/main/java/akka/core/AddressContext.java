package akka.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actor.Props;
import akka.actors.DefaultSenderActor;

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
     * 集群的actor地址维护  k: actorName  v:集群中actor引用
     */
    private Map<String, List<ActorRefMap>> map = new HashMap<>();

    /**
     * 集群模式下消息初始化actor 用作初始化接收方actorRef   key为actorName 不重复生成
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
        //集群地址列表预加载
        getSender(system, path);
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
