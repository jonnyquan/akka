package com.xkeshi.task.entities;


import com.xkeshi.task.enums.FileType;
import com.xkeshi.task.enums.PackageMethod;
import com.xkeshi.task.enums.ServiceSupport;
import com.xkeshi.task.enums.TaskStatus;

/**
 * Created by ruancl@xkeshi.com on 2017/1/12.
 */
public class ExportTask {

    private Long id;

    private String result;

    private TaskStatus taskStatus;

    private ServiceSupport serviceSupport;

    private String params;

    private FileType fileExt;

    private int multiFilesPackage;

    private PackageMethod packageMethod;

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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public FileType getFileExt() {
        return fileExt;
    }

    public void setFileExt(FileType fileExt) {
        this.fileExt = fileExt;
    }

    public int getMultiFilesPackage() {
        return multiFilesPackage;
    }

    public void setMultiFilesPackage(int multiFilesPackage) {
        this.multiFilesPackage = multiFilesPackage;
    }

    public PackageMethod getPackageMethod() {
        return packageMethod;
    }

    public void setPackageMethod(PackageMethod packageMethod) {
        this.packageMethod = packageMethod;
    }
}
