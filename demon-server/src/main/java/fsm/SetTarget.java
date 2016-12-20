package fsm;

import akka.actor.ActorRef;

/**
 * Created by ruancl@xkeshi.com on 2016/12/19.
 */
public final class SetTarget {
    private ActorRef actorRef;

    public SetTarget(ActorRef actorRef) {
        this.actorRef = actorRef;
    }

    public ActorRef getActorRef() {
        return actorRef;
    }
}
