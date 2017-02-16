package akka.core;

import akka.actor.ActorRef;
import akka.dispatch.*;
import akka.enums.RouterGroup;
import akka.msg.Message;
import akka.params.AskResponseResolver;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 * <p>
 * ask 方式的消息发送
 */
public class AskSenderWrapper extends AbstractSenderWrapper implements AskSender {


    private final Long time;
    private final ExecutionContext ec;
    private AskResponseResolver defaultAskResponseResolver;


    public AskSenderWrapper(String group, String gettersK, AskResponseResolver askResponseResolver, RouterGroup routerGroup, long timeOut, AbstractAkkaSystemContext akkaSystem) {
        super(group,gettersK, routerGroup, akkaSystem);
        this.defaultAskResponseResolver = askResponseResolver;
        this.ec = akkaSystem.getSystem().dispatcher();
        this.time = timeOut;
    }


    @Override
    public void handleMsg(Message message) {
       sendMsg(message,null,null);
    }


    @Override
    public Message sendMsgSync(Message message) {
        return sendMsg(message,null,new CountDownLatch(1));
    }

    @Override
    public Message sendMsgSync(Message message, AskResponseResolver askResponseResolver) {
        return sendMsg(message, askResponseResolver,new CountDownLatch(1));
    }

    private Message sendMsg(Message message, AskResponseResolver askResponseResolver, CountDownLatch countDownLatch){
        Message rs = Message.emptyMessage();
        final AskResponseResolver finalAskResponseResolver;
        if(askResponseResolver == null){
            finalAskResponseResolver = this.defaultAskResponseResolver;
        }else{
            finalAskResponseResolver = askResponseResolver;
        }
        FiniteDuration finiteDuration = Duration.create(time, TimeUnit.MILLISECONDS);
        final Timeout timeout = new Timeout(finiteDuration);

        ActorRef getter = getGetters();
        final Future future = Patterns.ask(getter, message, timeout);
        future.onFailure(new OnFailure() {
            @Override
            public void onFailure(Throwable throwable) {
                finalAskResponseResolver.onFailure(throwable,AskSenderWrapper.this,message);
            }
        }, ec);
        future.onSuccess(new OnSuccess() {
            @Override
            public void onSuccess(Object o) {
                finalAskResponseResolver.onSuccess(AskSenderWrapper.this,message, (Message) o);
                if(countDownLatch!=null){
                    rs.reflushSelf((Message)o);
                    countDownLatch.countDown();
                }
            }
        }, ec);
        future.onComplete(new OnComplete() {
            @Override
            public void onComplete(Throwable throwable, Object o)  {
                finalAskResponseResolver.onComplete(AskSenderWrapper.this, throwable, (Message) o);
            }
        }, ec);
        if(countDownLatch!=null && countDownLatch.getCount()>0){
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                //log error
            }
        }
        return rs;
    }

    @Override
    public void sendMsg(Message message, AskResponseResolver askResponseResolver) {
       sendMsg(message, askResponseResolver,null);
    }
}
