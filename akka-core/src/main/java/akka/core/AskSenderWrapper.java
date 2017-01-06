package akka.core;

import akka.dispatch.*;
import akka.enums.RouterGroup;
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
import java.util.concurrent.TimeUnit;


/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 * <p>
 * ask 方式的消息发送
 */
public class AskSenderWrapper<S, R> extends AbstractSenderWrapper {


    private final Long time = 20000l;
    private final ExecutionContext ec;
    private AskProcessHandler<S, R> askProcessHandler;


    public AskSenderWrapper(String group,String gettersK, AskProcessHandler<S, R> askProcessHandler, RouterGroup routerGroup, AbstractAkkaSystem akkaSystem) {
        super(group,gettersK, routerGroup, akkaSystem);
        this.askProcessHandler = askProcessHandler;
        this.ec = akkaSystem.getSystem().dispatcher();
    }


    @Override
    public Object handleMsg(Message message) {
        CutParam cutParam = new CutParam(message);
        FiniteDuration finiteDuration = Duration.create(time, TimeUnit.MILLISECONDS);
        final Timeout timeout = new Timeout(finiteDuration);
        final ArrayList<Future<Object>> futures = new ArrayList<Future<Object>>();

        getGetters().forEach(getter -> {
            final Future future = Patterns.ask(getter, askProcessHandler.cut(cutParam), timeout);
            future.onFailure(new OnFailure() {
                @Override
                public void onFailure(Throwable throwable) throws Throwable {
                    askProcessHandler.onFailure(throwable,AskSenderWrapper.this,message);
                }
            }, ec);
            future.onSuccess(new OnSuccess() {
                @Override
                public void onSuccess(Object o) throws Throwable {
                    askProcessHandler.onSuccess(AskSenderWrapper.this,message, (Message) o);
                }
            }, ec);
            future.onComplete(new OnComplete() {
                @Override
                public void onComplete(Throwable throwable, Object o) throws Throwable {
                    askProcessHandler.onComplete(AskSenderWrapper.this, throwable, (Message) o);
                }
            }, ec);
            futures.add(future);//任务切割 参数2为消息内容  需要与消息处理的地方类型统一
        });

        final Future<Iterable<Object>> aggregate = Futures.sequence(futures,
                ec);

        //Future<R> back =
        aggregate.map(
                new Mapper<Iterable<Object>, R>() {
                    public R apply(Iterable<Object> coll) {
                        return askProcessHandler.getReturn(coll.iterator());
                    }
                }
                , ec);
        // Patterns.pipe(back, ec).to(getSender());
        return null;
    }


}
