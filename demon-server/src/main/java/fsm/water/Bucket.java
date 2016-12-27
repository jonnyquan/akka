package fsm.water;

import akka.actor.AbstractFSM;


/**
 * Created by ruancl@xkeshi.com on 2016/12/20.
 */
public class Bucket extends AbstractFSM<BState, BucketData> {
    {
        startWith(BState.EMPTY, new WaterEmpty());

        when(BState.EMPTY, matchEvent(WaterEmpty.class,
                (empty, full) ->
                        goTo(BState.HALF).using(full)
        ));
        when(BState.HALF, matchEvent(WaterHalf.class,
                (half, full) ->
                        goTo(BState.FULL).using(full)
        ));
        when(BState.HALF, matchEvent(WaterHalf.class,
                (half, empty) ->
                        goTo(BState.EMPTY).using(empty)
        ));
        when(BState.FULL, matchEvent(WaterFull.class,
                (full, half) ->
                        goTo(BState.FULL).using(half)
        ));

        initialize();
    }
}
