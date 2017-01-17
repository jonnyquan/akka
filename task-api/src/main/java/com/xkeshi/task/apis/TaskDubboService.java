package com.xkeshi.task.apis;

import com.xkeshi.task.dtos.ImportTaskDTO;
import com.xkeshi.task.dtos.ExportTaskDTO;

import javax.validation.constraints.NotNull;

/**
 * Created by ruancl@xkeshi.com on 2017/1/13.
 */
public interface TaskDubboService {

    Long sendImportTask(@NotNull(message = "TASK_VALIDATION_001") ImportTaskDTO importTaskDTO);

    Long sendOuportTask(@NotNull(message = "TASK_VALIDATION_002") ExportTaskDTO outputTaskDTO);
}
