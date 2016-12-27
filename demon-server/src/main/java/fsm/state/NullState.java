package fsm.state;

/**
 * Created by ruancl@xkeshi.com on 2016/12/27.
 */
public class NullState extends State {
    @Override
    public void poll(WaterTown town) {
        System.out.println("没水了");

    }

    @Override
    public void push(WaterTown town) {
        System.out.println("加水--》半桶状态");
        town.setNowState(new HalfState());
    }
}
