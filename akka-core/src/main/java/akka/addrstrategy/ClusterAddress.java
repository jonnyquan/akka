package akka.addrstrategy;

import akka.actor.*;
import akka.actors.IdentityActor;
import akka.cluster.Cluster;
import akka.cluster.Member;
import akka.core.ActorRefMap;
import akka.enums.RouterGroup;
import akka.params.ActorAddress;
import scala.collection.Iterator;
import scala.collection.SortedSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 */
public class ClusterAddress implements AddressContext{



    private final ActorSystem system;
    /**
     * 集群的actor地址维护  k: actorName  v:集群中actor引用
     */
    private Map<String, List<ActorRefMap>> map = new HashMap<>();


    /**
     * 集群模式下消息初始化actor 用作初始化接收方actorRef   key为actorName 不重复生成
     */
    private ActorRef senderActor;


    public ClusterAddress(ActorSystem system) {
        this.system = system;
    }

    @Override
    public void initReceivers(String path, RouterGroup routerGroup) {

        if (senderActor == null) {
            senderActor = system.actorOf(Props.create(IdentityActor.class, this.map));
        }
        List<ActorRefMap> actorRefs = getActorRefs(path);
        if (actorRefs != null) {
            return;
        }
        actorRefs = new ArrayList<>();
        addMap(path, actorRefs);
        SortedSet<Member> memberSortedSet = Cluster.get(system).readView().members();
        if (memberSortedSet.size() == 0) {
            throw new NullPointerException("集群中没有可用地址,集群离线 or 未开启集群监听");
        }
        Iterator<Member> iterator = memberSortedSet.iterator();
        while(iterator.hasNext()){
            Member member = iterator.next();
            Address addr = member.address();
            system.actorSelection(String.format("%s/user/%s", addr.toString(), path)).tell(new Identify(new ActorAddress(addr,path)), senderActor);
        }
    }


    /**
     * 移除断线机器里面的actorRef
     * @param address
     */
    public void deleteAddress(Address address) {
            deleteActorRef(address);
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
            System.out.println("暂无可用客户端接收消息 客户端已下线 或者 未启用集群监听功能");
            return null;
        }
        return maps.stream().map(ActorRefMap::getV).collect(Collectors.toList());
    }
}
