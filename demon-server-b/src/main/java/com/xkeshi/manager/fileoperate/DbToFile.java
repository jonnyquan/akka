package com.xkeshi.manager.fileoperate;

import akka.actors.AbstractActor;
import akka.anntations.Actor;
import akka.msg.Message;
import beans.OutputTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.bouncycastle.asn1.ua.DSTU4145NamedCurves.params;

/**
 * Created by ruancl@xkeshi.com on 2017/1/5.
 * 从数据库数据导出到文件 返回文件地址
 */
@Actor(name = "dbToFile")
public class DbToFile extends AbstractActor {
    private static final Logger logger = LoggerFactory.getLogger(DbToFile.class);

    @Override
    protected void handleMsg(Message message) {
        if(message.getContent() instanceof OutputTask){
            OutputTask outputTask = (OutputTask) message.getContent();
            logger.info("server b:收到数据库导出文件任务,相关业务参数:"+outputTask.getUid()+":"+outputTask.getSize());

            logger.info("server b:根据参数查询数据库,执行导出成文件操作........");
            try {
                Thread.sleep(2000l);
            } catch (InterruptedException e) {
                logger.error("server b:线程休眠失败");
            }
            logger.info("server b:导出完成,反馈信息,生成文件:test.excel");
            reply(new Message("server b:完成导入操作,返回文件地址: E://test.excel"));
        }else{
            logger.error("无效的导出参数类型");
        }

    }
}
