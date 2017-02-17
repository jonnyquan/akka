/*
package com.xkeshi.task.services.compnents.mq.akka;

import akka.core.Sender;
import akka.msg.Message;
import akka.params.AbstractAskResponseResolver;

*/
/**
 * Created by ruancl@xkeshi.com on 2017/1/5.
 *//*

public class ExportResponseResolver extends AbstractAskResponseResolver {


    @Override
    protected void handleComplete(Throwable throwable, Message request) {
        System.out.println("complete");

    }

    @Override
    protected void handleSuccess(Message response) {
        System.out.println("收到反馈信息:"+response.getContent()+" 记录到成功列表,供用户下载");
    }

    @Override
    protected Boolean handleFailedAndReturnIfRetry(Throwable throwable) {
        return true;
    }

    @Override
    protected void retryGiveUp(Sender sender, Message message) {
        System.out.println("记录到缓存"+sender.getGroupName()+sender.getTopicName());
    }
}
*/
