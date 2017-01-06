package com.xkeshi.manager;

import akka.annotations.ActorRef;
import akka.core.Sender;
import akka.enums.RequestType;
import akka.enums.RouterGroup;
import akka.msg.Message;
import beans.ImportTask;
import beans.OutputTask;
import com.xkeshi.manager.fileoperate.DbInsertAskProcessHandler;
import com.xkeshi.manager.fileoperate.FileDownloadResponse;
import utils.FtpUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by ruancl@xkeshi.com on 2017/1/6.
 */
@Controller
@RequestMapping("/")
public class ApplicationControll {

    @ActorRef(group = "import",name = "fileToDb",request_type = RequestType.ASK,routerStrategy = RouterGroup.ROBIN,askHandle = DbInsertAskProcessHandler.class)
    private Sender ftb;


    @ActorRef(group = "output",name = "dbToFile",request_type = RequestType.ASK,routerStrategy = RouterGroup.ROBIN,askHandle = FileDownloadResponse.class)
    private Sender dtf;

    @ResponseBody
    @RequestMapping("input")
    public String input(@RequestParam(value = "file", required = false) MultipartFile file){

        String root = "/Users/huihui/Documents/";
        String path = root + "aaa.txt";
        try {
            path = FtpUtil.uploadFile(file.getInputStream(),path);
            //发起导入数据库指令
            //数据库写入一条导入指令状态
            Long uniqueId = 0l;
            ftb.sendMsg(new Message(new ImportTask(uniqueId,path)));

            return "文件上传成功,正在导入数据库!。。。。。。";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "系统异常 请稍后";
    }


    @ResponseBody
    @RequestMapping("output")
    public String output(@RequestParam("id") Integer id,@RequestParam("size")Integer size){
        Long uniqueId = 0l;
        //数据库插入一条记录  用于查询此次的文件生成状态

        dtf.sendMsg(new Message(new OutputTask(uniqueId,id,size)));

        return null;
    }

    @ResponseBody
    @RequestMapping("queryfile")
    public String queryFile(@RequestParam("queryId") String queryId){
        //通过数据库查询 上一次的文件生成状态
        return null;
    }



}
