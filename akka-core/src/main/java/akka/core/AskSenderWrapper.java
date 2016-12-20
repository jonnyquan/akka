package akka.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.dispatch.*;
import akka.enums.RouterStrategy;
import akka.enums.TransferType;
import akka.msg.Message;
import akka.params.AskProcessHandler;
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
 *
 * ask 方式的消息发送
 */
public class AskSenderWrapper<S, R> extends AbstractSenderWrapper {


    private final Long time = 5000l;
    private AskProcessHandler<S, R> askProcessHandler;


    public AskSenderWrapper(String gettersK, AddressContext addressContext, ActorSystem system, AskProcessHandler<S, R> askProcessHandler) {
        super(gettersK, addressContext, system);
        this.askProcessHandler = askProcessHandler;
    }


    @Override
    public Object handleMsg(Message message, RouterStrategy transferType) {
        List<ActorRef> actorRefs = getGetters(transferType);
        if (actorRefs == null) {
            return null;
        }
        CutParam cutParam = new CutParam(message);
        FiniteDuration finiteDuration = Duration.create(time, TimeUnit.MILLISECONDS);
        final Timeout timeout = new Timeout(finiteDuration);
        final ExecutionContext ec = getSystem().dispatcher();
        final ArrayList<Future<Object>> futures = new ArrayList<Future<Object>>();

        actorRefs.forEach(getter -> {
            final Future future = Patterns.ask(getter, askProcessHandler.cut(cutParam), timeout);
            future.onFailure(new OnFailure() {
                @Override
                public void onFailure(Throwable throwable) throws Throwable {
                    askProcessHandler.onFailure(getter, throwable, askProcessHandler, cutParam);
                }
            }, ec);
            future.onSuccess(new OnSuccess() {
                @Override
                public void onSuccess(Object o) throws Throwable {
                    askProcessHandler.onSuccess(getter, o);
                }
            }, ec);
            future.onComplete(new OnComplete() {
                @Override
                public void onComplete(Throwable throwable, Object o) throws Throwable {
                    askProcessHandler.onComplete(getter, throwable, o);
                }
            }, ec);
            futures.add(future);//任务切割 参数2为消息内容  需要与消息处理的地方类型统一
        });

        final Future<Iterable<Object>> aggregate = Futures.sequence(futures,
                ec);

        Future<R> back = aggregate.map(
                new Mapper<Iterable<Object>, R>() {
                    public R apply(Iterable<Object> coll) {
                        return askProcessHandler.getReturn(coll.iterator());
                    }
                }
                , ec);
        Patterns.pipe(back, ec).to(getSender());
        return null;
    }


}
