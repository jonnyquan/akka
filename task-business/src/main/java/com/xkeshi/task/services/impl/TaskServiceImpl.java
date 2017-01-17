package com.xkeshi.task.services.impl;

import com.xkeshi.task.dao.FileTaskDAO;
import com.xkeshi.task.entities.ImportTask;
import com.xkeshi.task.entities.ExportTask;
import com.xkeshi.task.enums.TaskStatus;
import com.xkeshi.task.services.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ruancl@xkeshi.com on 2017/1/10.
 *
 */
@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);


    @Autowired
    private FileTaskDAO fileTaskDAO;


    @Override
    public Long saveImportTask(ImportTask taskBean) {
        fileTaskDAO.saveImportTask(taskBean);
        return taskBean.getId();
    }

    @Override
    public Long saveOutputTask(ExportTask taskBean) {

        fileTaskDAO.saveOutputTask(taskBean);
        return taskBean.getId();
    }

    @Override
    public void updateOutputTaskResult(Long id, String s) {
        fileTaskDAO.updateOutputTaskResult(id,s, TaskStatus.SUCCESS);
    }

    @Override
    public void updateImportTaskResult(Long id, String s) {
        fileTaskDAO.updateImportTaskResult(id,s,TaskStatus.SUCCESS);
    }
}
