package com.xkeshi.task.services.compnents;

import com.xkeshi.task.dtos.ExportTaskDTO;
import com.xkeshi.task.dtos.ImportTaskDTO;
import com.xkeshi.task.handlers.DataProcessorLocator;
import com.xkeshi.task.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ruancl@xkeshi.com on 2017/2/14.
 */
@Component
public class FileProcessor {

  /**
   * dataServiceLocator 定位到相关的业务service进行执行
   */
  @Autowired
  private DataProcessorLocator dataProcessorLocator;

  @Autowired
  private TaskService taskService;

  public void processImport(ImportTaskDTO importTaskDTO){
    Object o = dataProcessorLocator.locationHandler(importTaskDTO.getServiceSupport()).handleImport(importTaskDTO);
    taskService.updateImportTaskResult(importTaskDTO.getId(),o.toString());
  }
  public void processExport(ExportTaskDTO exportTaskDTO){
    Object o = dataProcessorLocator.locationHandler(exportTaskDTO.getServiceSupport()).handleExport(exportTaskDTO);
    taskService.updateOutputTaskResult(exportTaskDTO.getId(),o.toString());
  }
}
