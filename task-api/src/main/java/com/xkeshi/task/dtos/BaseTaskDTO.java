package com.xkeshi.task.dtos;

import com.xkeshi.task.enums.ServiceSupport;
import com.xkeshi.task.enums.TaskStatus;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by ruancl@xkeshi.com on 2017/1/10.
 */
public class BaseTaskDTO implements Serializable {

    /**
     * 任务id 唯一自增
     */
    private Long id;

    /**
     * 任务生成后的结果反馈
     */
    private String result;

    /**
     * 任务状态
     */
    private TaskStatus taskStatus = TaskStatus.CREATE;

    /**
     * 业务支持模块
     */
    @NotNull(message = "TASK_VALIDATION_004")
    private ServiceSupport serviceSupport;

    public BaseTaskDTO(ServiceSupport serviceSupport) {
        this.serviceSupport = serviceSupport;
    }

    public BaseTaskDTO() {
    }

    private Date createTime;

    private Date updateTime;

    public ServiceSupport getServiceSupport() {
        return serviceSupport;
    }

    public void setServiceSupport(ServiceSupport serviceSupport) {
        this.serviceSupport = serviceSupport;
    }

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
