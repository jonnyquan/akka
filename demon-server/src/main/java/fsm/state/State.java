package fsm.state;

/**
 * Created by ruancl@xkeshi.com on 2016/12/27.
 */
public abstract class State {

    public abstract void poll(WaterTown town);

    public abstract void push(WaterTown town);
}
