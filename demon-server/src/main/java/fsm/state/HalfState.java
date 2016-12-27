package fsm.state;

/**
 * Created by ruancl@xkeshi.com on 2016/12/27.
 */
public class HalfState extends State {
    @Override
    public void poll(WaterTown town) {
        System.out.println("倒水-->空桶状态");
        town.setNowState(new NullState());
    }

    @Override
    public void push(WaterTown town) {
        System.out.println("加水-->满水状态");
        town.setNowState(new FullState());
    }
}
