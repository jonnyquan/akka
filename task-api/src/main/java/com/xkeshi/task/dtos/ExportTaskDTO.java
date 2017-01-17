package com.xkeshi.task.dtos;

import com.xkeshi.task.enums.FileType;
import com.xkeshi.task.enums.PackageMethod;
import com.xkeshi.task.enums.ServiceSupport;


/**
 * Created by ruancl@xkeshi.com on 2017/1/6.
 * 文件导出bean
 */
public class ExportTaskDTO<T> extends BaseTaskDTO {

    /**
     * 业务自定义参数
     */
    private T params;

    /**
     * 导出后文件格式
     */
    private FileType fileExt;

    /**
     * 数据过长时候  系统会自动切分多个文件(具体策略详见DataHandler)   切分后是否需要打包
     */
    private Boolean multiFilesPackage;

    /**
     * 打包的包类型
     */
    private PackageMethod packageMethod;

    public ExportTaskDTO(T params, ServiceSupport serviceSupport) {
        super(serviceSupport);
        this.params = params;
        this.fileExt = FileType.TXT;
        this.multiFilesPackage = true;
        this.packageMethod = PackageMethod.ZIP;
    }


    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }

    public FileType getFileExt() {
        return fileExt;
    }

    public void setFileExt(FileType fileExt) {
        this.fileExt = fileExt;
    }

    public Boolean getMultiFilesPackage() {
        return multiFilesPackage;
    }

    public void setMultiFilesPackage(Boolean multiFilesPackage) {
        this.multiFilesPackage = multiFilesPackage;
    }

    public PackageMethod getPackageMethod() {
        return packageMethod;
    }

    public void setPackageMethod(PackageMethod packageMethod) {
        this.packageMethod = packageMethod;
    }
}
