package fsm.water;

import akka.actor.AbstractFSM;

import static fsm.water.Uninitlied.Uninitialized;

/**
 * Created by ruancl@xkeshi.com on 2016/12/20.
 */
public class Bucket extends AbstractFSM<BState,BucketData> {
    {
        startWith(BState.EMPTY,Uninitialized);

        when(BState.EMPTY,matchEvent(WaterPush.class,Uninitlied.class,
                (waterPush,uninitlied)->
                        stay().using(new ToDo())
                ));


        initialize();
    }
}
