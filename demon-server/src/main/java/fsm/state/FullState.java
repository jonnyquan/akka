package fsm.state;

/**
 * Created by ruancl@xkeshi.com on 2016/12/27.
 */
public class FullState extends State {
    @Override
    public void poll(WaterTown town) {
        System.out.println("倒水--》半桶状态");
        town.setNowState(new HalfState());
    }

    @Override
    public void push(WaterTown town) {
        System.out.println("水满了");
    }
}
