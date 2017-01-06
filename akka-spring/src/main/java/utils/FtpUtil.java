package utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 2017/1/6.
 */
public class FtpUtil {
    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    private static String host = "localhost";
    private static int port = 21;
    private static String user = "huihui";
    private static String password = "4870025";


    public static FTPClient connectFtp(){
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host,port);
            ftpClient.login(user,password);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.info("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                logger.info("FTP连接成功。");
                // 设置PassiveMode传输
                ftpClient.enterLocalPassiveMode();
                // 设置以二进制流的方式传输
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.setControlEncoding("UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftpClient;
    }

    public static List<String> uploadFiles(Map<String,InputStream> pathFiles){
        FTPClient ftpClient = connectFtp();
        List<String> list = new ArrayList<>();
        pathFiles.forEach((k,v)->list.add(uploadFile(ftpClient,v,k)));
        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Map<String,InputStream> downLoadFiles(HashSet<String> filePaths){
        FTPClient ftpClient = connectFtp();
        Map<String,InputStream> map = filePaths.stream().collect(Collectors.toMap((p) -> p,(p) -> downLoadFile(ftpClient,p)));
        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static InputStream downLoadFile(FTPClient ftpClient,String filePath){
        InputStream in = null;
        try {
            int index = filePath.lastIndexOf("/");
            String fileName = filePath.substring(index);
            String fileDirectory = filePath.substring(0,index+1);
            ftpClient.changeWorkingDirectory(fileDirectory);
            in = ftpClient.retrieveFileStream(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
    private static InputStream downLoadFile(String filePath){
        FTPClient ftpClient = connectFtp();
        InputStream in = downLoadFile(ftpClient,filePath);
        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    public static String uploadFile(InputStream in,String rootPath){
        FTPClient ftpClient = connectFtp();
        rootPath = uploadFile(ftpClient,in,rootPath);
        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rootPath;
    }

    private static String uploadFile(FTPClient ftpClient,InputStream in,String filePath){
        String fileName;
        String fileDirectory;
        try {
            int index = filePath.lastIndexOf("/");
            fileName = filePath.substring(index);
            fileDirectory = filePath.substring(0,index+1);
            boolean directoryStatus = ftpClient.changeWorkingDirectory(fileDirectory);
            if(!directoryStatus){
                logger.info("目录 {} 不存在",fileDirectory);
            }
            boolean status = ftpClient.storeFile(filePath,in);
            in.close();
            logger.info("上传 {} 到ftp{} 地址:{}",fileName,status,filePath);
        } catch (FileNotFoundException e) {
            logger.info("ftp上文件不存在");
            filePath = null;
        } catch (IOException e) {
            logger.info("ftp上文件读取异常");
            filePath = null;
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return filePath;
    }
}
