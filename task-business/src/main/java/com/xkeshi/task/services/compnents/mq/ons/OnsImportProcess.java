package com.xkeshi.task.services.compnents.mq.ons;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.ParseException;
import com.aliyun.openservices.ons.api.Message;
import com.xkeshi.core.common.ons.AbstractMessageProcess;
import com.xkeshi.task.dtos.ExportTaskDTO;
import com.xkeshi.task.dtos.ImportTaskDTO;
import com.xkeshi.task.services.compnents.mq.FileProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
