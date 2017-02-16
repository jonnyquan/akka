package com.xkeshi.task.handlers;

import com.xkeshi.task.dtos.BaseTaskDTO;
import com.xkeshi.task.dtos.ImportTaskDTO;
import com.xkeshi.task.dtos.ExportTaskDTO;

/**
 * Created by ruancl@xkeshi.com on 2017/1/11.
 */
public interface DataProcessor {

     Object handleImport(ImportTaskDTO importTaskDTO);

     Object handleExport(ExportTaskDTO outputTaskDTO);


}
