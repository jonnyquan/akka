package com.xkeshi.task.entities;


import com.xkeshi.task.enums.ServiceSupport;
import com.xkeshi.task.enums.TaskStatus;

/**
 * Created by ruancl@xkeshi.com on 2017/1/12.
 */
public class ImportTask {

    private Long id;

    private String result;

    private TaskStatus taskStatus;

    private ServiceSupport serviceSupport;

    private String paths;


    private String importParam;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public ServiceSupport getServiceSupport() {
        return serviceSupport;
    }

    public void setServiceSupport(ServiceSupport serviceSupport) {
        this.serviceSupport = serviceSupport;
    }

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }

    public String getImportParam() {
        return importParam;
    }

    public void setImportParam(String importParam) {
        this.importParam = importParam;
    }
}
