package com.xkeshi.task.services;


import com.xkeshi.task.entities.ImportTask;
import com.xkeshi.task.entities.ExportTask;

/**
 * Created by ruancl@xkeshi.com on 2017/1/9.
 */
public interface TaskService {

    Long saveImportTask(ImportTask taskBean);

    Long saveOutputTask(ExportTask taskBean);

    void updateOutputTaskResult(Long id, String s);

    void updateImportTaskResult(Long id, String s);
}
