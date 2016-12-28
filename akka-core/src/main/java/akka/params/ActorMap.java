package akka.params;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.cluster.LoadBalance;

/**
 * Created by ruancl@xkeshi.com on 2016/12/28.
 */
public class ActorMap {

    private Address address;

    private ActorRef actorRef;

    private LoadBalance loadBalance;

    public ActorMap(Address address, ActorRef actorRef, LoadBalance loadBalance) {
        this.address = address;
        this.actorRef = actorRef;
        this.loadBalance = loadBalance;
    }

    public Address getAddress() {
        return address;
    }

    public ActorRef getActorRef() {
        return actorRef;
    }

    public LoadBalance getLoadBalance() {
        return loadBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActorMap actorMap = (ActorMap) o;

        return address.equals(actorMap.address);

    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}
