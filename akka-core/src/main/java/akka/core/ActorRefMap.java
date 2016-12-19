package akka.core;

import akka.actor.ActorRef;
import akka.actor.Address;

/**
 * Created by ruancl@xkeshi.com on 16/11/9.
 */
public class ActorRefMap {

    private Address k;
    private ActorRef v;

    public ActorRefMap(Address k, ActorRef v) {
        this.k = k;
        this.v = v;
    }

    public Boolean containAddr(Address address) {
        return address.toString().equals(k.toString());
    }

    public Address getK() {
        return k;
    }

    public ActorRef getV() {
        return v;
    }
}
