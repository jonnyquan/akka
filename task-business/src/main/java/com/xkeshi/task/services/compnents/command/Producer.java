package com.xkeshi.task.services.compnents.command;

import com.xkeshi.task.dtos.ExportTaskDTO;
import com.xkeshi.task.dtos.ImportTaskDTO;

/**
 * Created by ruancl@xkeshi.com on 2017/2/13.
 *
 */
public interface Producer {

  void sendImportTask(ImportTaskDTO importTask);

  void sendExportTask(ExportTaskDTO exportTask);
}
