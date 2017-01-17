package com.xkeshi.task.services.compnents;

import akka.msg.Message;
import akka.params.AbstractAskResponseResolver;

/**
 * Created by ruancl@xkeshi.com on 2017/1/5.
 */
public class ImportResponseResolver extends AbstractAskResponseResolver {


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
