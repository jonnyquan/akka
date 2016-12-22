package akka.addrstrategy;

import akka.actor.*;
import akka.cluster.Cluster;
import akka.cluster.Member;
import akka.core.AbstractAkkaSystem;
import akka.core.ActorRefMap;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.enums.RouterGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.Iterator;
import scala.collection.SortedSet;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 */
public class ClusterAddress {

    private static final Logger logger = LoggerFactory.getLogger(ClusterAddress.class);


    private final ActorSystem system;
    /**
     * 集群的actor地址维护  k: actorName  v:集群中actor引用(思考有没有并发问题)
     */
    private Map<String, Map<Address,ActorRef>> map;

    private static final Long TIME = 1000l;

    private final FiniteDuration finiteDuration;


    public ClusterAddress(ActorSystem system) {
        this.system = system;
        this.map = new HashMap<>(0);
        this.finiteDuration = Duration.create(TIME, TimeUnit.MILLISECONDS);
    }

    public void initReceivers(String path, RouterGroup routerGroup) {

        List<ActorRefMap> actorRefs = getActorRefs(path);
        if (actorRefs != null) {
            return;
        }
        actorRefs = new ArrayList<>();
        final List<ActorRefMap> refs = actorRefs;
        addMap(path, actorRefs);
        SortedSet<Member> memberSortedSet = Cluster.get(system).readView().members();
        if (memberSortedSet.size() == 0) {
            throw new NullPointerException("集群中没有可用地址,集群离线 or 未开启集群监听");
        }
        final CountDownLatch countDownLatch = new CountDownLatch(memberSortedSet.size());
        Iterator<Member> iterator = memberSortedSet.iterator();
        final ExecutionContextExecutor executionContextExecutor = system.dispatcher();
        while(iterator.hasNext()){
            Member member = iterator.next();
            Address addr = member.address();
            Future<ActorRef> future = system.actorSelection(String.format("%s/user/%s", addr.toString(), path)).resolveOne(finiteDuration);
                    future.onSuccess(new OnSuccess<ActorRef>() {
                @Override
                public void onSuccess(ActorRef actorRef) throws Throwable {
                    logger.info(addr+":"+path+"地址被发现"+System.currentTimeMillis());
                    countDownLatch.countDown();
                    synchronized (refs) {
                        refs.add(new ActorRefMap(addr, actorRef));
                    }
                }
            },executionContextExecutor);
            future.onFailure(new OnFailure() {
                @Override
                public void onFailure(Throwable throwable) throws Throwable {
                    countDownLatch.countDown();
                }
            },executionContextExecutor);
        }
        if(countDownLatch.getCount()>0){
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info(path+":接收地址初始化成功cluster");
    }

    /**
     * 当机子上线时候  先剔除原先该机子的actor  并重新讲actorRef放入
     * @param address
     */
    public void addressUp(Address address){
        for(String key : map.keySet()){

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
            for (String key : map.keySet()) {
                map.get(key).remove(address);
            }
    }


    public synchronized void addMap(String key, List<ActorRefMap> actorRefMaps) {
        if (map.containsKey(key)) {
            throw new IllegalArgumentException("请勿重复生成消息任务");
        }
        synchronized (map) {
            map.put(key, actorRefMaps);
        }
    }



    public List<ActorRef> getReceivers(String name,RouterGroup routerGroup){
        Map<Address,ActorRef> refs = map.get(name);
        if (refs==null || refs.size()==0) {
            System.out.println("暂无可用客户端接收消息 客户端已下线 或者 未启用集群监听功能");
            return null;
        }
        Collector.of(refs.values());
        return Arrays.asList(refs.values());
    }
}
