package com.xkeshi.task.services.compnents.command.remote.ons;

import com.aliyun.openservices.ons.api.Message;
import com.xkeshi.core.common.ons.AbstractMessageProcess;
import com.xkeshi.task.dtos.ImportTaskDTO;
import com.xkeshi.task.services.compnents.FileProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ruancl@xkeshi.com on 2017/2/13.
 */
public class OnsImportProcess extends AbstractMessageProcess {

  @Autowired
  private FileProcessor fileProcessor;

  @Override
  protected void doProcess(Message message) {
    byte[] body = message.getBody();
      ImportTaskDTO importTaskDTO = (ImportTaskDTO) SerializeUtil.getObjectFromBytes(body);
      fileProcessor.processImport(importTaskDTO);
  }
}
