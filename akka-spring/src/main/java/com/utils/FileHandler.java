package com.utils;


import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 2017/1/10.
 */
public interface FileHandler {

    /**
     *
     * @param in
     * @param ext  文件后缀   不含'.'
       * @return
     */
    String uploadFile(InputStream in,String ext);

    List<String> uploadFiles(List<FileEntry> pathFiles);
    /**
     *
     * @param filePath  下载路径
     * @return
     */
    InputStream downLoadFile(String filePath);

    Map<String,InputStream> downLoadFiles(HashSet<String> filePaths);


}
