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
import org.springframework.beans.factory.annotation.Value;

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
    /**
     * 每个文件里面的最大记录条数
     */
    @Value("${single.file.rows}")
    final private int SINGLE_FILE_ROWS = 1;
    /**
     * 每次查询的最大记录数
     */
    @Value("${page.size.one.query}")
    final private int PAGE_SIZE_ONE_QUERY = 2;

    public AbstractDataProcessor(FileHandler fileHandler) {
        //no spring
        this.fileHandler = fileHandler;
    }

    public AbstractDataProcessor() {
    }

    public abstract ServiceSupport matchServiceSupport();

    protected abstract List<R> selectDb(PO param,int offset,int countSize);

    protected abstract boolean transferBytesToObjectAndInsertIntoDb(PI importParam,byte[] bytes);

    protected abstract byte[] transferDataToByte(List<R> list);



    private Object writeDbFromFile(ImportTaskDTO<PI> importTaskDTO){
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

  /**
   * 每个文件限制记录条数  对每次查询出来的记录数进行切割
   * @return
   */
  private List<List<R>> cutData(List<R> list){
        if(CollectionUtils.isEmpty(list)){
            throw new NullPointerException("未查到数据");
        }
        //分割结果 文件切割
        List<List<R>> cutRs = new ArrayList<>();
        int size = list.size();
        if(size>SINGLE_FILE_ROWS){
            int mod = cutIntoSlicesCount(size,SINGLE_FILE_ROWS);

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
        }else{
            cutRs.add(list);
        }
        return cutRs;
    }

    private List<String> writeData(String ext,PackageMethod packageExt,boolean ifPackage,List<List<R>> cutRs){
        List<String> paths;
        List<FileEntry> entries = new ArrayList<>();
        if(ifPackage){
            List<InputStream> inputStreamList = new ArrayList<>();
              cutRs.forEach(rs ->
                  inputStreamList.add(new ByteArrayInputStream(transferDataToByte(rs)))
              );
            String path = fileHandler.uploadFile(FileUtil.packageFiles(inputStreamList,ext,packageExt.getName()),packageExt.getName());
            paths = Arrays.asList(path);
        }else{
          cutRs.forEach(rs ->
              entries.add(new FileEntry(ext,new ByteArrayInputStream(transferDataToByte(rs))))
          );
          paths = fileHandler.uploadFiles(entries);
        }
        return paths;
    }


    private Object writeFileFromDb(ExportTaskDTO<PO> outputTaskDTO){
        //根据自己的参数定义查询各自数据库
        int rowCount = outputTaskDTO.getRouCount();
        List<Page> pages = null;
        if(rowCount>PAGE_SIZE_ONE_QUERY){
            pages  = cutIntoSlices(rowCount);
        }else{
            pages = Arrays.asList(new Page(0,rowCount));
        }
        final PO param = outputTaskDTO.getParams();
        String ext = outputTaskDTO.getFileExt().getName();
        final PackageMethod packageMethod = outputTaskDTO.getPackageMethod();
        final Boolean multiFilesPac = outputTaskDTO.getMultiFilesPackage();
        StringBuilder sb = new StringBuilder();
        pages.forEach(page -> {
            List<String> paths = writeData(
                ext,
                packageMethod,
                multiFilesPac,
                cutData(
                    selectDb(param,page.offSet,page.pageSize)
                ));
            paths.forEach(p->sb.append(p).append(':'));
        });
      return sb.toString();
    }

    private int cutIntoSlicesCount(int total,int section){
        return total%section == 0 ? total/section : total/section + 1;
    }
  /**
   * 分页查询切分
   */
  private List<Page> cutIntoSlices(int rowCount){
      int pageCount = cutIntoSlicesCount(rowCount,PAGE_SIZE_ONE_QUERY);
      List<Page> pages = new ArrayList<>();
      while(pageCount >0){
          pageCount --;
          pages.add(new Page(pageCount*PAGE_SIZE_ONE_QUERY,PAGE_SIZE_ONE_QUERY));
      }
      return pages;
  }

  private class Page{
        int offSet;
        int pageSize;

        public Page(int offSet, int pageSize) {
            this.offSet = offSet;
            this.pageSize = pageSize;
        }
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
