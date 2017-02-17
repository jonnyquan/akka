package com.xkeshi.task.services.compnents.command.remote.ons;

import com.aliyun.openservices.ons.api.Message;
import com.xkeshi.core.common.ons.AbstractMessageProcess;
import com.xkeshi.task.dtos.ExportTaskDTO;
import com.xkeshi.task.services.compnents.FileProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ruancl@xkeshi.com on 2017/2/13.
 */
public class OnsExportProcess extends AbstractMessageProcess {

  @Autowired
  private FileProcessor fileProcessor;

  @Override
  protected void doProcess(Message message) {
    byte[] body = message.getBody();
      ExportTaskDTO exportTaskDTO = (ExportTaskDTO) SerializeUtil.getObjectFromBytes(body);
      fileProcessor.processExport(exportTaskDTO);
  }
}
