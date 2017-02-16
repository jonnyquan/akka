package com.xkeshi.task.services.compnents.mq.ons;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.ParseException;
import com.aliyun.openservices.ons.api.Message;
import com.xkeshi.core.common.ons.AbstractMessageProcess;
import com.xkeshi.task.dtos.ExportTaskDTO;
import com.xkeshi.task.services.compnents.mq.FileProcessor;
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
