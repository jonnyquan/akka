/*
package com.xkeshi.task.services.compnents.mq.akka;

import akka.actors.AbstractAkkaService;
import akka.actors.Reply;
import akka.msg.MessageStatus;
import com.annotations.Actor;
import akka.msg.Message;
import com.xkeshi.task.dtos.ExportTaskDTO;
import com.xkeshi.task.handlers.DataProcessorLocator;
import com.xkeshi.task.services.TaskService;
import com.xkeshi.task.services.compnents.mq.FileProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

*/
/**
 * Created by ruancl@xkeshi.com on 2017/1/5.
 * 从数据库数据导出到文件 返回文件地址
 *//*

@Actor(name = "dbToFile",number = 10)
@Component
public class ExportConsumerActor extends AbstractAkkaService {
    private static final Logger logger = LoggerFactory.getLogger(ExportConsumerActor.class);

   @Autowired
   private FileProcessor fileProcessor;

    @Override
    public void handleMsg(Message message, Reply reply) {
        if(message.getContent() instanceof ExportTaskDTO){
            ExportTaskDTO outputTaskDTO = (ExportTaskDTO) message.getContent();
            fileProcessor.processExport(outputTaskDTO);
        }else{
            message.setMessageStatus(MessageStatus.REFUSE);
        }
        reply.reply(message);
    }

}
*/
