package akka.params;

import akka.actor.Address;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 */
public class ActorAddress {

    private Address address;
    private String actorName;

    public ActorAddress(Address address, String actorName) {
        this.address = address;
        this.actorName = actorName;
    }

    public Address getAddress() {
        return address;
    }

    public String getActorName() {
        return actorName;
    }
}
