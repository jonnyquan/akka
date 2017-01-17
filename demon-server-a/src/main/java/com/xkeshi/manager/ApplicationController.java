package com.xkeshi.manager;

import com.utils.FileHandler;
import com.xkeshi.task.apis.TaskDubboService;
import com.xkeshi.task.dtos.ImportTaskDTO;
import com.xkeshi.task.dtos.ExportTaskDTO;
import com.xkeshi.task.enums.ServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by ruancl@xkeshi.com on 2017/1/6.
 */
@Controller
@RequestMapping("/")
public class ApplicationController {



    @Autowired
    private TaskDubboService taskDubboService;

    @Autowired
    private FileHandler fileHandler;

    @ResponseBody
    @RequestMapping("input")
    public String input(@RequestParam(value = "file") MultipartFile file){

        String ext = "txt";
        try {
            String path = fileHandler.uploadFile(file.getInputStream(),ext);
            HashSet<String> paths = new HashSet<>();
            paths.add(path);
            //数据库写入一条导入指令状态 可以用dubbo调用任务数据  此处使用akka来模拟同步
            ImportTaskDTO taskBean = new ImportTaskDTO(paths,ServiceSupport.PRODUCT,"3");
            Long uniqueId = taskDubboService.sendImportTask(taskBean);
            return "upload file success,doing insert in to database!。。。。。。"+uniqueId;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "system error please wait...";
    }


    @ResponseBody
    @RequestMapping("output")
    public String output(@RequestParam("name")String name,@RequestParam("offset") Integer offset,@RequestParam("size")Integer size){
        //数据库插入一条记录  用于查询此次的文件生成状态  dubbo调用  此处用akka模拟
        ExportTaskDTO outputTaskDTO = new ExportTaskDTO(name+","+offset+","+size,ServiceSupport.PRODUCT);
        Long uniqueId =  taskDubboService.sendOuportTask(outputTaskDTO);
        return "file creating...,please wait a moment and use the file key:' "+uniqueId+" ' to find the file";
    }

    @ResponseBody
    @RequestMapping("queryfile")
    public String queryFile(@RequestParam("key") String key){
        //通过数据库查询 上一次的文件生成状态 和地址相关信息


        return null;
    }



}
