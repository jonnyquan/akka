package akka.actors;

import akka.actor.Address;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.core.AddressContext;

/**
 * Created by ruancl@xkeshi.com on 16/9/29.
 */
public class ClusterActor extends UntypedActor {

    Cluster cluster = Cluster.get(getContext().system());

    private AddressContext addressContext;

    public ClusterActor(AddressContext addressContext) {
        this.addressContext = addressContext;
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
            System.out.println("member up :" + member);
            addressContext.addAddress(address);
            System.out.println("roles:" + memberUp.member().getRoles() + " || " + member.roles());
        } else if (o instanceof ClusterEvent.MemberRemoved) {
            System.out.println("member removed :" + ((ClusterEvent.MemberRemoved) o).member());
        } else if (o instanceof ClusterEvent.UnreachableMember) {
            ClusterEvent.UnreachableMember unreachableMember = (ClusterEvent.UnreachableMember) o;
            Address address = unreachableMember.member().address();
            addressContext.deleteAddress(address);
            // addressesMap.deleteAddress(address);
            System.out.println("member unreachable :" + ((ClusterEvent.UnreachableMember) o).member());
        } else if (o instanceof ClusterEvent.MemberEvent) {
            // ignore

        } else {
            unhandled(o);
        }
    }
}
