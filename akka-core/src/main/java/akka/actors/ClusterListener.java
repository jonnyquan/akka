package akka.actors;

import akka.actor.Address;
import akka.actor.UntypedActor;
import akka.addrstrategy.ClusterAddress;
import akka.balancestrategy.ServerStatus;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.cluster.metrics.ClusterMetricsChanged;
import akka.cluster.metrics.ClusterMetricsExtension;
import akka.cluster.metrics.NodeMetrics;
import akka.cluster.metrics.StandardMetrics;
import akka.core.AddressStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruancl@xkeshi.com on 16/9/29.
 *
 * clusterAddress需要观察集群的变动  serverStatu需要观察服务器的状态
 */
public class ClusterListener extends UntypedActor {

    private static final Logger logger = LoggerFactory.getLogger(ClusterListener.class);

    //防止启动时候up操作 判断下该地址是否已经有存在
    private List<Address> addresses;

    private Cluster cluster;

    private ClusterAddress clusterAddress;

    private ServerStatus serverStatus;

    private ClusterMetricsExtension clusterMetricsExtension;


    public ClusterListener(ClusterAddress clusterAddress,ServerStatus serverStatus) {
        this.clusterAddress = clusterAddress;
        this.cluster = Cluster.get(getContext().system());
        this.addresses = new ArrayList<>();
        this.clusterMetricsExtension = ClusterMetricsExtension.get(getContext().system());
        this.serverStatus = serverStatus;
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

        if (o instanceof ClusterEvent.MemberUp) {
            ClusterEvent.MemberUp memberUp = (ClusterEvent.MemberUp) o;
            Member member = memberUp.member();
            Address address = member.address();
            if(!addresses.contains(address)) {
                addresses.add(address);
            }else{
                clusterAddress.addressUp(member.address());
            }
           logger.info("member up :" + member);
        } else if (o instanceof ClusterEvent.MemberRemoved) {
            System.out.println("member removed :" + ((ClusterEvent.MemberRemoved) o).member());
        } else if (o instanceof ClusterEvent.UnreachableMember) {
            ClusterEvent.UnreachableMember unreachableMember = (ClusterEvent.UnreachableMember) o;
            Address address = unreachableMember.member().address();
            clusterAddress.deleteAddress(address);
            logger.info("member unreachable :" + ((ClusterEvent.UnreachableMember) o).member());
        } else if (o instanceof ClusterEvent.MemberEvent) {
            // ignore

        } else if (o instanceof ClusterMetricsChanged) {
            ClusterMetricsChanged clusterMetrics = (ClusterMetricsChanged) o;
                serverStatus.showStatus(clusterMetrics.getNodeMetrics());


        } else if (o instanceof ClusterEvent.CurrentClusterState) {
            // Ignore.
        } else {
            unhandled(o);
        }
    }


}
