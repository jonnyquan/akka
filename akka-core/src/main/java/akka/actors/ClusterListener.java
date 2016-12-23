package akka.actors;

import akka.actor.Address;
import akka.actor.UntypedActor;
import akka.addrstrategy.ClusterAddress;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.core.AddressStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruancl@xkeshi.com on 16/9/29.
 *
 * clusterAddress需要监听集群的变动
 */
public class ClusterListener extends UntypedActor {

    //防止启动时候up操作 判断下该地址是否已经有存在
    private List<Address> addresses;

    Cluster cluster;

    private ClusterAddress clusterAddress;

    public ClusterListener(ClusterAddress clusterAddress) {
        this.clusterAddress = clusterAddress;
        this.cluster = Cluster.get(getContext().system());
        this.addresses = new ArrayList<>();
    }

    @Override
    public void preStart() throws Exception {
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class);
    }

    @Override
    public void postStop() throws Exception {
        cluster.unsubscribe(getSelf());
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
            System.out.println("member up :" + member);
        } else if (o instanceof ClusterEvent.MemberRemoved) {
            System.out.println("member removed :" + ((ClusterEvent.MemberRemoved) o).member());
        } else if (o instanceof ClusterEvent.UnreachableMember) {
            ClusterEvent.UnreachableMember unreachableMember = (ClusterEvent.UnreachableMember) o;
            Address address = unreachableMember.member().address();
            clusterAddress.deleteAddress(address);
            System.out.println("member unreachable :" + ((ClusterEvent.UnreachableMember) o).member());
        } else if (o instanceof ClusterEvent.MemberEvent) {
            // ignore

        } else {
            unhandled(o);
        }
    }
}
