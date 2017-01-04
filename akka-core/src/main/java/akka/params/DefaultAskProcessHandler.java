package akka.params;

import akka.actor.ActorRef;
import akka.msg.Message;
import akka.params.AskProcessHandler;
import akka.params.CutParam;
import akka.pattern.AskTimeoutException;

import java.util.Iterator;

/**
 * Created by ruancl@xkeshi.com on 16/11/9.
 * 默认的ask模式  消息过程处理handler
 */
public class DefaultAskProcessHandler<S,R> extends AskProcessHandlerAdapt<S,R> {

    @Override
    public void onSuccess(ActorRef actorRef, Object o) {
        System.out.println(actorRef.path() + ":success:-----------object:" + ((Message) o).getContent());
    }

    @Override
    public void onFailure(ActorRef actorRef, Throwable throwable, AskProcessHandler<S, R> askProcessHandler, CutParam cutParam) {
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
