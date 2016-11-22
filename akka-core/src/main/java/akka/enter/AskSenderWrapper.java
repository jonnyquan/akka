package akka.enter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.*;
import akka.msg.Message;
import akka.params.AskHandle;
import akka.params.CutParam;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 */
public class AskSenderWrapper<S, R> extends MsgSenderWrapper {


    private final Long time = 5000l;
    private AskHandle<S, R> askHandle;


    public AskSenderWrapper(String gettersK, AddressContex addressContex, ActorSystem system, AskHandle<S, R> askHandle) {
        super(gettersK, addressContex, system);
        this.askHandle = askHandle;
    }


    @Override
    public Object handleMsg(Message message, Boolean ifCluster) {
        List<ActorRef> actorRefs = getGetters(ifCluster);
        if (actorRefs == null) {
            return null;
        }
        CutParam cutParam = new CutParam(message);
        FiniteDuration finiteDuration = Duration.create(time, TimeUnit.MILLISECONDS);
        final Timeout timeout = new Timeout(finiteDuration);
        final ExecutionContext ec = getSystem().dispatcher();
        final ArrayList<Future<Object>> futures = new ArrayList<Future<Object>>();

        actorRefs.forEach(getter -> {
            final Future future = Patterns.ask(getter, askHandle.cut(cutParam), timeout);
            future.onFailure(new OnFailure() {
                @Override
                public void onFailure(Throwable throwable) throws Throwable {
                    askHandle.onFailure(getter, throwable, askHandle, cutParam);
                }
            }, ec);
            future.onSuccess(new OnSuccess() {
                @Override
                public void onSuccess(Object o) throws Throwable {
                    askHandle.onSuccess(getter, o);
                }
            }, ec);
            future.onComplete(new OnComplete() {
                @Override
                public void onComplete(Throwable throwable, Object o) throws Throwable {
                    askHandle.onComplete(getter, throwable, o);
                }
            }, ec);
            futures.add(future);//任务切割 参数2为消息内容  需要与消息处理的地方类型统一
        });

        final Future<Iterable<Object>> aggregate = Futures.sequence(futures,
                ec);

        Future<R> back = aggregate.map(
                new Mapper<Iterable<Object>, R>() {
                    public R apply(Iterable<Object> coll) {
                        return askHandle.getReturn(coll.iterator());
                    }
                }
                , ec);
        Patterns.pipe(back, ec).to(getSender());
        return null;
    }


}
