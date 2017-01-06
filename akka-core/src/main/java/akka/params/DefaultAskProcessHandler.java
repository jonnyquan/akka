package akka.params;

import akka.actor.ActorRef;
import akka.core.Sender;
import akka.msg.Message;
import akka.pattern.AskTimeoutException;

/**
 * Created by ruancl@xkeshi.com on 16/11/9.
 * 默认的ask模式  消息过程处理handler
 */
public class DefaultAskProcessHandler<S, R> extends AskProcessHandlerAdapt<S, R> {


    @Override
    protected void handleComplete(Throwable throwable, Message request) {

    }

    @Override
    protected void handleSuccess(Message response) {
        System.out.println(":success:-----------object:" + response.getContent());
    }

    @Override
    protected Boolean handleFailedAndReturnIfRetry(Throwable throwable) {
        System.out.println(throwable.getCause());
        return false;
    }


}
