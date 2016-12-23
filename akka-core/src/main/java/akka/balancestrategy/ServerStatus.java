package akka.balancestrategy;

import akka.actors.ClusterListener;
import akka.cluster.metrics.NodeMetrics;
import akka.cluster.metrics.StandardMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 2016/12/23.
 */
public class ServerStatus {

    private static final Logger logger = LoggerFactory.getLogger(ServerStatus.class);

    private List<LoadBalance> loadBalances = new ArrayList<>();

    public void showStatus(Iterable<NodeMetrics> nodeMetrics) {
        notifyListener(nodeMetrics);
    }

    private void notifyListener(Iterable<NodeMetrics> nodeMetrics){
        loadBalances.forEach(o->o.updateServerStatu(nodeMetrics));
    }

   private void logHeap(NodeMetrics nodeMetrics) {
        StandardMetrics.HeapMemory heap = StandardMetrics.extractHeapMemory(nodeMetrics);
        if (heap != null) {
            logger.info("Used heap: {} MB", ((double) heap.used()) / 1024 / 1024);
        }
    }

   private  void logCpu(NodeMetrics nodeMetrics) {
        StandardMetrics.Cpu cpu = StandardMetrics.extractCpu(nodeMetrics);
        if (cpu != null && cpu.systemLoadAverage().isDefined()) {
            logger.info("Load: {} ({} processors)", cpu.systemLoadAverage().get(),
                    cpu.processors());
        }
    }

    public void onServerSubcribe(LoadBalanceStrategy loadBalanceStrategy) {
        loadBalances.addAll(loadBalanceStrategy.getLoadBalances().stream().filter(o->o.needListenStatus()).collect(Collectors.toList()));
    }
}
