/*
package com.xkeshi.task.services.compnents.mq.akka;

import akka.core.AskSender;
import akka.enums.RouterGroup;
import akka.msg.Message;
import com.annotations.AskActorRef;
import com.xkeshi.task.dtos.ExportTaskDTO;
import com.xkeshi.task.dtos.ImportTaskDTO;
import com.xkeshi.task.services.compnents.mq.MqProvider;
import org.springframework.stereotype.Component;

*/
/**
 * Created by ruancl@xkeshi.com on 2017/2/13.
 *//*

@Component
public class AkkaMqProvider implements MqProvider {


  @AskActorRef(group = "import",name = "fileToDb",routerStrategy = RouterGroup.ROBIN,askHandle = ImportResponseResolver.class)
  private AskSender ftb;


  @AskActorRef(group = "output",name = "dbToFile",routerStrategy = RouterGroup.ROBIN,askHandle = ExportResponseResolver.class)
  private AskSender dtf;

  @Override
  public void sendImportTask(ImportTaskDTO importTask) {
    ftb.sendMsg(new Message(importTask));
  }

  @Override
  public void sendExportTask(ExportTaskDTO exportTask) {
    dtf.sendMsg(new Message(exportTask));
  }
}
*/
