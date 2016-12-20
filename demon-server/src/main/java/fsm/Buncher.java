package fsm;

import akka.actor.AbstractFSM;
import akka.actor.ActorRef;

/**
 * Created by ruancl@xkeshi.com on 2016/12/19.
 */
enum State{
    UNVERIFIED,COMMIT,ROLLBACK
}


public class Buncher extends AbstractFSM<State, Data> {
    {
        startWith(fsm.State.UNVERIFIED, new LightData());

        when(fsm.State.UNVERIFIED,
                matchEvent(SetTarget.class,Uninitialized.class,(setTarget,uninitialized)->goTo(fsm.State.COMMIT).using(uninitialized))
        );

        initialize();
    }

}
