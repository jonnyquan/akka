package akka.routerstrategy;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.actor.Props;
import akka.actors.DefaultSenderActor;
import akka.core.ActorRefMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 */
public class ClusterStrategy {

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



    public void existActor(String path) {
        ActorRef senderActor = sender.get(path);
        if (senderActor == null) {
            senderActor = system.actorOf(Props.create(DefaultSenderActor.class, this, path));
            sender.put(path, senderActor);
        }
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

    private List<ActorRefMap> getActorRefs(String key) {
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

    public List<ActorRef> getReceivers(String name){
        List<ActorRefMap> maps = getActorRefs(name);
        if (maps==null || maps.size()==0) {
            System.out.println("暂无可用客户端接收消息");
            return null;
        }
        return maps.stream().map(ActorRefMap::getV).collect(Collectors.toList());
    }
}
