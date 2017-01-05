package akka.params;


import java.util.Iterator;

/**
 * Created by ruancl@xkeshi.com on 2017/1/4.
 */
public abstract class AskProcessHandlerAdapt<S, R> implements AskProcessHandler<S, R> {

    @Override
    public R getReturn(Iterator<Object> it) {
        return (R) it.next();
    }

    @Override
    public S cut(CutParam<S> cutParam) {
        return cutParam.getMsg();
    }
}
