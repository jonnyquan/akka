package akka.enter;

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

    public Boolean removeAdd(Address address) {
        if (address.toString().equals(k.toString())) {
            return true;
        }
        return false;
    }

    public Address getK() {
        return k;
    }

    public ActorRef getV() {
        return v;
    }
}
