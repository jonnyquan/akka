package com.xkeshi.manager.fileoperate;

import akka.actors.AbstractActor;
import akka.anntations.Actor;
import akka.enums.RouterPool;
import akka.msg.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ruancl@xkeshi.com on 2017/1/5.
 */
@Actor(name = "fileToDb",pool = RouterPool.ROBIN,number = 10)
public class FileToDb extends AbstractActor {
    private static final Logger logger = LoggerFactory.getLogger(FileToDb.class);

    @Override
    protected void handleMsg(Message message) {
        String path = message.getContent().toString();
        logger.info("server b:收到文件导入任务,文件地址:"+path);
        logger.info("server b:读取文件内容,执行导入数据库操作........");
        try {
            Thread.sleep(2000l);
        } catch (InterruptedException e) {
            logger.error("server b:线程失败");
        }
        logger.info("server b:插入完成,反馈信息");
        reply(new Message("server b:完成导入操作"));
    }
}
