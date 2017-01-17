package com.xkeshi.task.dao;

import com.xkeshi.task.entities.ImportTask;
import com.xkeshi.task.entities.ExportTask;
import com.xkeshi.task.enums.TaskStatus;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ruancl@xkeshi.com on 2017/1/10.
 */
public interface FileTaskDAO {



    int saveOutputTask(ExportTask taskBean);

    int saveImportTask(ImportTask taskBean);

    void updateOutputTaskResult(@Param("id")Long id, @Param("rs")String s,@Param("status")TaskStatus status);

    void updateImportTaskResult(@Param("id")Long id, @Param("rs")String s,@Param("status")TaskStatus status);
}
