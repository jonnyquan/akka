package akka.cluster.addrs;

import akka.actor.Address;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterContext;
import akka.cluster.LoadBalance;
import akka.cluster.Member;
import akka.cluster.metrics.NodeMetrics;
import akka.cluster.metrics.StandardMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 * <p>
 * 此集群策略 将启用自定义负载均衡器  监听集群地址 监听服务器状态
 * 由子类实现 对actorRef的维护
 */
public abstract class ClusterAddress implements ClusterContext {
    private static final Logger logger = LoggerFactory.getLogger(ClusterAddress.class);
    private List<LoadBalance> addrLoadBalance;
    private List<LoadBalance> serverLoadBalance;
    private List<Address> addresses;


    public ClusterAddress() {
        this.addrLoadBalance = new ArrayList<>();
        this.serverLoadBalance = new ArrayList<>();
        this.addresses = new ArrayList<>();
    }

    /**
     * 部分路由策略类需要监听 actorRef的变动
     * @param actorRefMap
     */
    protected void nodifyAddrListener(Set<Address> actorRefMap) {
        addrLoadBalance.forEach(o -> o.updateAddr(actorRefMap));
    }

    /**
     * 当机子上线时候  先剔除原先该机子的actor  并重新讲actorRef放入
     *
     * @param address
     */
    abstract void addressUp(Address address);

    /**
     * 移除断线机器里面的actorRef
     *
     * @param address
     */
    abstract void addressDown(Address address);

    protected void onSubcribe(LoadBalance loadBalance) {
        if (loadBalance.needListenAddr()) {
            addrLoadBalance.add(loadBalance);
        }
        if (loadBalance.needListenStatus()) {
            serverLoadBalance.add(loadBalance);
        }
    }


    public void notifyServerObserver(Iterable<NodeMetrics> nodeMetrics) {
        serverLoadBalance.forEach(o -> o.updateServerStatu(nodeMetrics));
    }

    public void notifyAddrObserver(Object o) {
        if (o instanceof ClusterEvent.MemberUp) {
            ClusterEvent.MemberUp memberUp = (ClusterEvent.MemberUp) o;
            Member member = memberUp.member();
            Address address = member.address();
            if (!addresses.contains(address)) {
                addresses.add(address);
            } else {
                addressUp(member.address());
            }
            logger.info("member up :" + member);
        } else if (o instanceof ClusterEvent.MemberRemoved) {
            System.out.println("member removed :" + ((ClusterEvent.MemberRemoved) o).member());
        } else if (o instanceof ClusterEvent.UnreachableMember) {
            ClusterEvent.UnreachableMember unreachableMember = (ClusterEvent.UnreachableMember) o;
            Address address = unreachableMember.member().address();
            addressDown(address);
            logger.info("member unreachable :" + ((ClusterEvent.UnreachableMember) o).member());
        } else if (o instanceof ClusterEvent.MemberEvent) {
            // ignore

        }
    }


    private void logHeap(NodeMetrics nodeMetrics) {
        StandardMetrics.HeapMemory heap = StandardMetrics.extractHeapMemory(nodeMetrics);
        if (heap != null) {
            logger.info("Used heap: {} MB", ((double) heap.used()) / 1024 / 1024);
        }
    }

    private void logCpu(NodeMetrics nodeMetrics) {
        StandardMetrics.Cpu cpu = StandardMetrics.extractCpu(nodeMetrics);
        if (cpu != null && cpu.systemLoadAverage().isDefined()) {
            logger.info("Load: {} ({} processors)", cpu.systemLoadAverage().get(),
                    cpu.processors());
        }
    }

}
