package fsm.state;

/**
 * Created by ruancl@xkeshi.com on 2016/12/27.
 */
public class WaterTown {

    private State nowState;

    public WaterTown(State nowState) {
        this.nowState = nowState;
    }

    public void setNowState(State nowState) {
        this.nowState = nowState;
    }

    public void add() {
        nowState.push(this);
    }

    public void dec() {
        nowState.poll(this);
    }
}
