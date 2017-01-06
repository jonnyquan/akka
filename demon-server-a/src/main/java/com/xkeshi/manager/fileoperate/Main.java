package com.xkeshi.manager.fileoperate;

import utils.FtpUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by ruancl@xkeshi.com on 2016/12/15.
 * 不依赖spring容器
 */
public class Main {

    public static void main(String[] args) {
       /* Akka akka = AkkaMain.initAkka();
        Sender ask = akka.createAskSender("import","fileToDb", new DbInsertAskProcessHandler() , RouterGroup.RANDOM);
        //导入文件到数据库 发送任务消息
        ask.sendMsg(new Message("F://test.excel"));

        //----------------------------------------
        Sender toFile = akka.createAskSender("output","dbToFile", new FileDownloadResponse() , RouterGroup.RANDOM);
        //从数据库导出到文件
        toFile.sendMsg(new Message("params"));*/
        File file1 = new File("/Users/huihui/Downloads/jetty-deploy.xml");
        try {
            FtpUtil.uploadFile(new FileInputStream(file1),"/Users/huihui/Documents/test.xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
