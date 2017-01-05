package akka.actors;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.addrs.AbstractClusterAddress;
import akka.cluster.metrics.ClusterMetricsChanged;
import akka.cluster.metrics.ClusterMetricsExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ruancl@xkeshi.com on 16/9/29.
 * <p>
 * clusterAddress需要观察集群的变动 以及服务器的状态
 */
public class ClusterListener extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(ClusterListener.class);

    private Cluster cluster;

    private AbstractClusterAddress abstractClusterAddress;

    private ClusterMetricsExtension clusterMetricsExtension;


    public ClusterListener(AbstractClusterAddress abstractClusterAddress) {
        this.abstractClusterAddress = abstractClusterAddress;
        this.cluster = Cluster.get(getContext().system());
        this.clusterMetricsExtension = ClusterMetricsExtension.get(getContext().system());
    }

    @Override
    public void preStart() throws Exception {
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class);
        clusterMetricsExtension.subscribe(getSelf());
    }

    @Override
    public void postStop() throws Exception {
        cluster.unsubscribe(getSelf());
        clusterMetricsExtension.unsubscribe(getSelf());
    }

    @Override
    public void onReceive(Object o) throws Throwable {

        if (o instanceof ClusterEvent
                || o instanceof ClusterEvent.MemberRemoved
                || o instanceof ClusterEvent.UnreachableMember
                ) {
            abstractClusterAddress.notifyAddrObserver(o);
        } else if (o instanceof ClusterMetricsChanged) {
            ClusterMetricsChanged clusterMetrics = (ClusterMetricsChanged) o;
            abstractClusterAddress.notifyServerObserver(clusterMetrics.getNodeMetrics());
        } else if (o instanceof ClusterEvent.CurrentClusterState) {
            // Ignore.
        } else {
            unhandled(o);
        }
    }


}
