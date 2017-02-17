package com.xkeshi.task.services.compnents.command.remote.ons;

import com.aliyun.openservices.ons.api.Message;
import com.xkeshi.core.common.ons.ONSProducerService;
import com.xkeshi.task.dtos.ExportTaskDTO;
import com.xkeshi.task.dtos.ImportTaskDTO;
import com.xkeshi.task.services.compnents.command.Producer;
import com.xkeshi.task.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by ruancl@xkeshi.com on 2017/2/13.
 */
@Component
public class OnsMqProducer implements Producer {

  @Autowired
  private ONSProducerService onsProducerService;

  @Value("${task.topic.name}")
  private String topic;

  @Override
  public void sendImportTask(ImportTaskDTO importTask) {
    onsProducerService.publish(new Message(topic, Constant.TAG_IMPORT, SerializeUtil.getbytesFromObject(importTask)));
  }

  @Override
  public void sendExportTask(ExportTaskDTO exportTask) {
    onsProducerService.publish(new Message(topic, Constant.TAG_EXPORT,SerializeUtil.getbytesFromObject(exportTask)));
  }
}

