package akka.params;

import akka.actor.ActorRef;
import akka.msg.Message;
import akka.pattern.AskTimeoutException;

import java.util.Iterator;

/**
 * Created by ruancl@xkeshi.com on 16/11/9.
 */
public class DefaultAskHandle implements AskHandle<Message, Message> {
    @Override
    public Message getReturn(Iterator<Object> it) {
        return (Message) it.next();
    }

    @Override
    public Message cut(CutParam<Message> cutParam) {
        return cutParam.getMsg();
    }

    @Override
    public void onSuccess(ActorRef actorRef, Object o) {
        System.out.println(actorRef.path() + ":success:-----------object:" + o);
    }

    @Override
    public void onFailure(ActorRef actorRef, Throwable throwable, AskHandle<Message, Message> askHandle, CutParam cutParam) {
        System.out.println("failure------------------" + throwable);
        if (throwable instanceof AskTimeoutException) {
            System.out.println(actorRef.path() + ":链接超时 ");
        }
        //同时记录失败信息  进行重发
    }

    @Override
    public void onComplete(ActorRef actorRef, Throwable throwable, Object o) {
        System.out.println("complete");
    }


}
