package fsm.state;

/**
 * Created by ruancl@xkeshi.com on 2016/12/27.
 */
public class Main {
    public static void main(String[] args) {
        WaterTown waterTown = new WaterTown(new NullState());
        waterTown.add();
        waterTown.add();
        waterTown.add();
        waterTown.dec();
        waterTown.dec();
        waterTown.dec();
    }
}
