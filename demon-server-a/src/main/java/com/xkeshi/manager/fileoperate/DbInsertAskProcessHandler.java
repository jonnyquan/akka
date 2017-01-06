package com.xkeshi.manager.fileoperate;

import akka.msg.Message;
import akka.params.AskProcessHandlerAdapt;

/**
 * Created by ruancl@xkeshi.com on 2017/1/5.
 */
public class DbInsertAskProcessHandler extends AskProcessHandlerAdapt<Message,Message> {


    @Override
    protected Boolean handleFailedAndReturnIfRetry(Throwable throwable) {
        return true;
    }


    @Override
    protected void handleComplete(Throwable throwable, Message request) {
        System.out.println("导入完成");

    }

    @Override
    protected void handleSuccess(Message response) {
        System.out.println("收到反馈信息:"+response.getContent());

    }
}
