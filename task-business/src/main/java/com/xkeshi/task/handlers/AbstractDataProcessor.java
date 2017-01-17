package com.xkeshi.task.handlers;

import com.utils.FileEntry;
import com.utils.FileHandler;
import com.xkeshi.task.dtos.ImportTaskDTO;
import com.xkeshi.task.dtos.ExportTaskDTO;
import com.xkeshi.task.enums.PackageMethod;
import com.xkeshi.task.enums.ServiceSupport;
import com.xkeshi.task.utils.FileUtil;
import com.xkeshi.core.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.*;

/**
 * Created by ruancl@xkeshi.com on 2017/1/10.
 * PI  导入操作时候的参数对象
 * PO  导出操作时候的参数对象
 * R   数据库操作对象
 *
 *
 */
public abstract class AbstractDataProcessor<PI,PO,R> implements DataProcessor {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDataProcessor.class);

    @Autowired
    private FileHandler fileHandler;

    final private int SINGLE_FILE_ROWS = 1;


    public AbstractDataProcessor() {
    }

    public AbstractDataProcessor(FileHandler fileHandler) {
        //no spring
        this.fileHandler = fileHandler;
    }


    public abstract ServiceSupport matchServiceSupport();

    protected abstract List<R> selectDb(PO param);

    protected abstract boolean transferBytesToObjectAndInsertIntoDb(PI importParam,byte[] bytes);

    protected abstract byte[] transferDataToByte(List<R> list);



    public Object writeDbFromFile(ImportTaskDTO<PI> importTaskDTO){
        HashSet<String> paths = importTaskDTO.getPaths();
        if(CollectionUtils.isEmpty(paths)){
            throw new NullPointerException("未发现任何可以下载的路径");
        }
        Map<String,InputStream> map = fileHandler.downLoadFiles(paths);
        if(map == null){
            throw new NullPointerException("找不到可以下载的文件");
        }
        StringBuilder sb = new StringBuilder();
        map.forEach(
                (k,o)->{
                    ByteArrayOutputStream tempArray = new ByteArrayOutputStream();
                    try {
                        byte[] buff = new byte[1024];
                        int rc;
                        while ((rc = o.read(buff, 0, 1024)) > 0) {
                            tempArray.write(buff, 0, rc);
                        }
                            //根据各自规则将流转换成自己的对象 并插入数据库
                        if(transferBytesToObjectAndInsertIntoDb(importTaskDTO.getImportParam(),tempArray.toByteArray())){
                            sb.append(k).append(" : ").append("ok; ");
                        }else{
                            sb.append(k).append(" : ").append("error; ");
                        }

                    }catch (IOException e) {
                        logger.error("io流读取异常");
                    } finally {
                        try {
                            o.close();
                            tempArray.close();
                        } catch (IOException e) {
                            logger.error("io流关闭异常");
                        }
                    }});
        return sb.toString();

    }



    public Object writeFileFromDb(ExportTaskDTO<PO> outputTaskDTO){
        //根据自己的参数定义查询各自数据库
        List<R> list = selectDb(outputTaskDTO.getParams());
        if(CollectionUtils.isEmpty(list)){
            throw new NullPointerException("未查到数据");
        }
        //分割结果
        List<List<R>> cutRs = new ArrayList<>();
        int size = list.size();
        if(size>SINGLE_FILE_ROWS){
            int mod = size % SINGLE_FILE_ROWS ==0 ? size/SINGLE_FILE_ROWS : size%SINGLE_FILE_ROWS+1;

            for(int i=0;i<mod;i++){
                cutRs.add(new ArrayList<>(SINGLE_FILE_ROWS));
            }
            int j = 0;
            for(R r : list){
                while(cutRs.get(j).size() == SINGLE_FILE_ROWS){
                    j ++;
                }
                cutRs.get(j).add(r);
            }
        }
        List<FileEntry> entries = new ArrayList<>();
        String ext = outputTaskDTO.getFileExt().getName();
        PackageMethod packageExt = outputTaskDTO.getPackageMethod();
        boolean ifPackage = outputTaskDTO.getMultiFilesPackage();
        List<String> paths;
        if(ifPackage){
            List<InputStream> inputStreamList = new ArrayList<>();
            for(int i=0;i<cutRs.size();i++){
                inputStreamList.add(new ByteArrayInputStream(transferDataToByte(cutRs.get(i))));
            }
            String path = fileHandler.uploadFile(FileUtil.packageFiles(inputStreamList,ext,packageExt.getName()),packageExt.getName());
            paths = Arrays.asList(path);
        }else{
            entries.add(new FileEntry(ext,new ByteArrayInputStream(transferDataToByte(list))));
            paths = fileHandler.uploadFiles(entries);
        }
        StringBuilder sb = new StringBuilder();
        for(String p : paths){
            sb.append(p).append(":");
        }

        return sb.toString();
    }

    @Override
    public Object handleImport(ImportTaskDTO importTaskDTO) {
        return this.writeDbFromFile(importTaskDTO);
    }

    @Override
    public Object handleExport(ExportTaskDTO outputTaskDTO) {
        return this.writeFileFromDb(outputTaskDTO);
    }




}
