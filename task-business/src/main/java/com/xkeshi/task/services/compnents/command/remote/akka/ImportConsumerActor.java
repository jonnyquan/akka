/*
package com.xkeshi.task.services.compnents.mq.akka;

import akka.actors.AbstractAkkaService;
import akka.actors.Reply;
import akka.msg.MessageStatus;
import com.annotations.Actor;
import akka.enums.RouterPool;
import akka.msg.Message;
import com.xkeshi.task.dtos.ImportTaskDTO;
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
 *//*

@Actor(name = "fileToDb",pool = RouterPool.ROBIN,number = 10)
@Component
public class ImportConsumerActor extends AbstractAkkaService {

    private static final Logger logger = LoggerFactory.getLogger(ImportConsumerActor.class);

    @Autowired
    private FileProcessor fileProcessor;


    @Override
    public void handleMsg(Message message, Reply reply) {
        Object obj = message.getContent();
        if(obj instanceof ImportTaskDTO){
            ImportTaskDTO taskBean = (ImportTaskDTO) obj;
            fileProcessor.processImport(taskBean);
            logger.info("server b:插入完成,反馈信息");
        }else{
            message.setMessageStatus(MessageStatus.REFUSE);
        }
        reply.reply(message);

    }
}
*/
