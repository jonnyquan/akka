package com.utils;

import com.xkeshi.core.utils.DateUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 2017/1/6.
 *     文件夹规则  每天创建一个文件夹存放文件   文件名使用uuid
 *     文件夹应该在目录下面预先生成好 范围可以是当前一年
 */
public class FtpHandlerImpl implements FileHandler {
    private static Logger logger = LoggerFactory.getLogger(FtpHandlerImpl.class);

    private final String host;
    //= "localhost";
    private final int port;
    //= 21;
    private final String user;
    // = "huihui";
    private final String password;
    //= "4870025";

    private final String root;



    public FtpHandlerImpl(String host, int port, String user, String password, String root) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.root = root;
    }

    public  FTPClient connectFtp(){
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

    public  List<String> uploadFiles(List<FileEntry> pathFiles){
        FTPClient ftpClient = connectFtp();
        List<String> list = new ArrayList<>();
        pathFiles.forEach(o->list.add(uploadFile(ftpClient,o.getInputStream(),o.getExt())));
        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public  Map<String,InputStream> downLoadFiles(HashSet<String> filePaths){
        FTPClient ftpClient = connectFtp();
        Map<String,InputStream> map = filePaths.stream().collect(Collectors.toMap((p) -> p,(p) -> downLoadFile(ftpClient,p)));
        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }



    private  InputStream downLoadFile(FTPClient ftpClient,String filePath){
        InputStream in = null;
        try {
            int index = filePath.lastIndexOf("/");
            String fileDirectory = filePath.substring(0,index+1);
            ftpClient.changeWorkingDirectory(fileDirectory);
            in = ftpClient.retrieveFileStream(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }
    public  InputStream downLoadFile(String filePath){
        FTPClient ftpClient = connectFtp();
        InputStream in = downLoadFile(ftpClient,filePath);
        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    public  String uploadFile(InputStream in,String ext){
        FTPClient ftpClient = connectFtp();
        String returnPath = uploadFile(ftpClient,in,ext);
        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnPath;
    }

    private  String uploadFile(FTPClient ftpClient,InputStream in,String ext){

        String fileName;
        String fileDirectory;
        String filePath;
        try {
            fileName = UUID.randomUUID().toString()+"."+ext;
            fileDirectory = DateUtils.getNow(DateUtils.FORMAT_INT_DATE);
            filePath = root+fileDirectory +"/"+ fileName;
            boolean status = ftpClient.storeFile(filePath,in);
            if(!status){
                logger.error("上传失败,可能是目录 {} 不存在,请先检查目录",fileDirectory);
            }
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

    public static void main(String[] args) {
        FtpHandlerImpl ftpHandler = new FtpHandlerImpl("localhost",21,"huihui","4870025","/Users/huihui/Documents/");
        InputStream inputStream = new ByteArrayInputStream("hhh".getBytes());
        ftpHandler.uploadFile(inputStream,"jpg");
    }
}
