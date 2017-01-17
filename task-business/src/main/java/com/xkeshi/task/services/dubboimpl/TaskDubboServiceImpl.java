package com.xkeshi.task.services.dubboimpl;

import akka.core.AskSender;
import akka.enums.RouterGroup;
import akka.msg.Message;
import com.annotations.AskActorRef;
import com.xkeshi.task.apis.TaskDubboService;
import com.xkeshi.task.dtos.ImportTaskDTO;
import com.xkeshi.task.dtos.ExportTaskDTO;
import com.xkeshi.task.entities.ImportTask;
import com.xkeshi.task.entities.ExportTask;
import com.xkeshi.task.services.compnents.ImportResponseResolver;
import com.xkeshi.task.services.compnents.ExportResponseResolver;
import com.xkeshi.task.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ruancl@xkeshi.com on 2017/1/13.
 */
@Service
public class TaskDubboServiceImpl implements TaskDubboService {

    @AskActorRef(group = "import",name = "fileToDb",routerStrategy = RouterGroup.ROBIN,askHandle = ImportResponseResolver.class)
    private AskSender ftb;


    @AskActorRef(group = "output",name = "dbToFile",routerStrategy = RouterGroup.ROBIN,askHandle = ExportResponseResolver.class)
    private AskSender dtf;

    @Autowired
    private TaskService taskService;

    @Override
    public Long sendImportTask(ImportTaskDTO taskBean) {
        //任务数据插入
        ImportTask importTask = new ImportTask();
        importTask.setId(taskBean.getId());
        importTask.setImportParam(taskBean.getImportParam().toString());
        StringBuilder sb = new StringBuilder();
        taskBean.getPaths().forEach(o->sb.append(o).append(";"));
        importTask.setPaths(sb.toString());
        importTask.setServiceSupport(taskBean.getServiceSupport());
        importTask.setTaskStatus(taskBean.getTaskStatus());

        Long id = taskService.saveImportTask(importTask);

        taskBean.setId(id);
        //异步任务发起
        ftb.sendMsg(new Message(taskBean));
        return id;
    }

    @Override
    public Long sendOuportTask(ExportTaskDTO taskBean) {
        //任务数据插入
        ExportTask exportTask = new ExportTask();
        exportTask.setTaskStatus(taskBean.getTaskStatus());
        exportTask.setServiceSupport(taskBean.getServiceSupport());
        exportTask.setId(taskBean.getId());
        exportTask.setFileExt(taskBean.getFileExt());
        exportTask.setMultiFilesPackage(taskBean.getMultiFilesPackage() ? 1:0);
        exportTask.setPackageMethod(taskBean.getPackageMethod());
        exportTask.setParams(taskBean.getParams().toString());

        Long id = taskService.saveOutputTask(exportTask);
        taskBean.setId(id);
        //异步任务发起
        dtf.sendMsg(new Message(taskBean));
        return id;
    }
}
