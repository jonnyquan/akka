package akka.params;

import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 16/11/9.
 * 默认的ask模式  消息过程处理handler
 */
public class DefaultAskResponseResolver extends AbstractAskResponseResolver {


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
