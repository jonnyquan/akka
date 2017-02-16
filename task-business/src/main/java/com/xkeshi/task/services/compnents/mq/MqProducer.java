package com.xkeshi.task.services.compnents.mq;

import com.xkeshi.task.dtos.ExportTaskDTO;
import com.xkeshi.task.dtos.ImportTaskDTO;

/**
 * Created by ruancl@xkeshi.com on 2017/2/13.
 *
 */
public interface MqProducer {

  void sendImportTask(ImportTaskDTO importTask);

  void sendExportTask(ExportTaskDTO exportTask);
}
