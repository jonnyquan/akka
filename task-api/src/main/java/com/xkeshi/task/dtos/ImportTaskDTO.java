package com.xkeshi.task.dtos;

import com.xkeshi.task.enums.ServiceSupport;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.HashSet;

/**
 * Created by ruancl@xkeshi.com on 2017/1/6.
 * 文件导入任务bean
 */
public class ImportTaskDTO<I> extends BaseTaskDTO {

    /**
     * 文件上传后返回的文件地址  支持多文件导入
     */
    @NotEmpty(message = "TASK_VALIDATION_003")
    private HashSet<String> paths;

    /**
     * 业务自定义所需要的参数 注意序列化
     */
    private I importParam;

    public ImportTaskDTO(HashSet<String> path, ServiceSupport serviceSupport, I importParam) {
        super(serviceSupport);
        this.paths = path;
        this.importParam = importParam;
    }


    public I getImportParam() {
        return importParam;
    }

    public void setImportParam(I importParam) {
        this.importParam = importParam;
    }


    public HashSet<String> getPaths() {
        return paths;
    }

    public void setPaths(HashSet<String> paths) {
        this.paths = paths;
    }
}
