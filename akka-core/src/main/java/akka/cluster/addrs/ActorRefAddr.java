package akka.cluster.addrs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.cluster.Cluster;
import akka.cluster.LoadBalance;
import akka.cluster.Member;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.enums.RouterGroup;
import akka.params.ActorMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.collection.Iterator;
import scala.collection.SortedSet;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 2016/12/27.
 */
public class ActorRefAddr extends ClusterAddress {

    private static final Logger logger = LoggerFactory.getLogger(ActorRefAddr.class);
    private static final Long TIME = 1000l;
    private final ActorSystem system;
    private final FiniteDuration finiteDuration;
    /**
     * 集群的actor地址维护  k: actorName  v:集群中actor引用(思考有没有并发问题)
     */
    private Map<String, Map<Address, ActorMap>> map;

    /**
     * 服务器掉线后 先放入offline  等服务器上线时候 从offLine里面捡回来
     */
    private Map<String,Map<Address,ActorMap>> offLine;

    public ActorRefAddr(ActorSystem system) {
        this.system = system;
        this.map = new HashMap<>(0);
        this.offLine = new HashMap<>();
        this.finiteDuration = Duration.create(TIME, TimeUnit.MILLISECONDS);
    }

    private void selectActor(Address addr, String path, final CountDownLatch countDownLatch,final RouterGroup routerGroup) {
        final ExecutionContextExecutor executionContextExecutor = system.dispatcher();
        Future<ActorRef> future = system.actorSelection(String.format("%s/user/%s", addr.toString(), path)).resolveOne(finiteDuration);
        future.onSuccess(new OnSuccess<ActorRef>() {
            @Override
            public void onSuccess(ActorRef actorRef) throws Throwable {
                logger.info(addr + ":" + path + "地址被发现" + System.currentTimeMillis());
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
                Map<Address, ActorMap> refs = map.get(path);
                LoadBalance loadBalance = routerGroup.createAndGetLoadBalance();
                //注册监听
                onSubcribe(loadBalance);
                refs.put(addr, new ActorMap(addr,actorRef,loadBalance));
            }
        }, executionContextExecutor);
        future.onFailure(new OnFailure() {
            @Override
            public void onFailure(Throwable throwable) throws Throwable {
                if (countDownLatch != null) {
                    countDownLatch.countDown();
                }
            }
        }, executionContextExecutor);
    }

    /**
     * 发射初始化地址时候  吧需要监听的负载均衡类加入list
     *
     * @param path
     */
    @Override
    public void initReceiversAndBalance(String path, RouterGroup routerGroup) {
        Map<Address, ActorMap> actorRefs = map.get(path);
        if (actorRefs != null) {
            return;
        }
        actorRefs = new HashMap<>();
        addMap(path, actorRefs);
        SortedSet<Member> memberSortedSet = Cluster.get(system).readView().members();
        if (memberSortedSet.size() == 0) {
            throw new NullPointerException("集群中没有可用地址,集群离线 or 未开启集群监听");
        }
        final CountDownLatch countDownLatch = new CountDownLatch(memberSortedSet.size());
        Iterator<Member> iterator = memberSortedSet.iterator();
        while (iterator.hasNext()) {
            Member member = iterator.next();
            Address addr = member.address();
            selectActor(addr, path, countDownLatch,routerGroup);
        }
        if (countDownLatch.getCount() > 0) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info(path + ":接收地址初始化成功cluster");
    }

    private void addMap(String key, Map<Address, ActorMap> actorRefMaps) {
        if (map.containsKey(key)) {
            throw new IllegalArgumentException("请勿重复生成消息任务");
        }
        map.put(key, actorRefMaps);
    }

    @Override
    public List<ActorRef> getReceivers(String name, RouterGroup routerGroup) {
        Map<Address, ActorMap> refs = map.get(name);
        if (refs == null || refs.size() == 0) {
            System.out.println("暂无可用客户端接收消息 客户端已下线 或者 未启用集群监听功能");
            return null;
        }
        LoadBalance loadBalance = refs.get(name).getLoadBalance();
        if (loadBalance == null) {
            return refs.values().stream().collect(Collectors.toList());
        }
        return Arrays.asList(loadBalance.router(refs));
    }

    /**
     * 当机子上线时候  先剔除原先该机子的actor  并重新讲actorRef放入
     *
     * @param address
     */
    @Override
    public void addressUp(Address address) {
        logger.info(address + "上线,actor重载");
        for (String key : map.keySet()) {
            //selectActor(address, key, null);
            //从垃圾桶中恢复
            map.get(key).put(address,offLine.get(key).get(address));
            nodifyAddrListener(map.get(key));
        }
    }

    /**
     * 移除断线机器里面的actorRef
     *
     * @param address
     */
    @Override
    public void addressDown(Address address) {
        logger.info(address + "掉线,actor移除");
        for (String key : map.keySet()) {
            Map<Address,ActorMap> leave = map.get(key);
            Map<Address,ActorMap> off =  offLine.get(key);
            if(off == null){
                off = new HashMap<>();
            }
            off.put(address,leave.get(address));
            leave.remove(address);
            nodifyAddrListener(map.get(key));
        }
    }

}
