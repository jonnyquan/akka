package com.xkeshi.task.utils;

import java.io.*;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by ruancl@xkeshi.com on 2017/1/12.
 */
public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);


    /**
     * 打包
     * @param list
     * @param fileExt
     * @param packageExt
     * @return
     */
    public static InputStream packageFiles(List<InputStream> list, String fileExt, String packageExt){
        InputStream in = null;
        ZipOutputStream zipOutputStream = null;
        try {
            File tempFile = File.createTempFile(UUID.randomUUID().toString(),packageExt);
            zipOutputStream = new ZipOutputStream(new FileOutputStream(tempFile));
            for(InputStream o : list){
                try {
                    zipOutputStream.putNextEntry(new ZipEntry(UUID.randomUUID().toString()+"."+fileExt));
                    byte[] buff = new byte[1024];
                    int rc;
                    while ((rc = o.read(buff, 0, 1024)) > 0) {
                        zipOutputStream.write(buff, 0, rc);
                    }
                } catch (IOException e) {
                    logger.info("io exception");
                }finally {
                    try {
                        o.close();
                        zipOutputStream.closeEntry();
                    } catch (IOException e) {
                        logger.error("io exception");
                    }
                }

            }
            in = new FileInputStream(tempFile);
        } catch (FileNotFoundException e) {
            logger.info("file not find");
        } catch (IOException e) {
            logger.info("io exception");
        }finally {
           closeOutPutStream(zipOutputStream);
        }


        return in;
    }

    public static void closeOutPutStream(OutputStream... outputStreams){
        try {

            for(OutputStream outputStream : outputStreams){
            if(outputStream!=null){
                    outputStream.close();
            }
        }
        } catch (IOException e) {
            logger.error("io exception");
        }
    }

    public static void closeInPutStream(InputStream... inputStreams){
        try {

            for(InputStream inputStream : inputStreams){
                if(inputStream!=null){
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            logger.error("io exception");
        }
    }


}
