package akka.params;


import akka.core.Sender;
import akka.msg.Message;

import java.util.Iterator;

/**
 * Created by ruancl@xkeshi.com on 2017/1/4.
 */
public abstract class AskProcessHandlerAdapt<S, R> implements AskProcessHandler<S, R> {

    /**
     * 重试次数
     */
    final protected static int RETRY_TIMES = 3;

    /**
     * 重试间隔
     */
    final protected static long RETRY_INTERVAL = 3000l;

    @Override
    public R getReturn(Iterator<Object> it) {
        return (R) it.next();
    }

    @Override
    public S cut(CutParam<S> cutParam) {
        return cutParam.getMsg();
    }

    @Override
    public void onFailure(Throwable throwable, Sender sender, Message request) {
        if(handleFailedAndReturnIfRetry(throwable)){
            retry(sender,request);
        }
    }

    @Override
    public void onSuccess(Sender sender, Message request, Message response) {
        handleSuccess(response);
    }


    @Override
    public void onComplete(Sender sender, Throwable throwable, Message request) {
        handleComplete(throwable,request);
    }

    protected abstract void handleComplete(Throwable throwable, Message request);

    protected abstract void handleSuccess(Message response);

    protected abstract Boolean handleFailedAndReturnIfRetry(Throwable throwable);

    protected void retry(Sender sender, Message message){
        try {
            Thread.sleep(RETRY_INTERVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(message.retry()<RETRY_TIMES){
            System.out.println("消息重发");
            sender.sendMsg(message);
        }else{
            retryGiveUp(sender,message);
        }
    }

    /**
     * 可重新改方法
     * @param sender
     * @param message
     */
    protected void retryGiveUp(Sender sender, Message message){
        System.out.println("重试次数过多  记录数据库  不再重试");
    }
}
