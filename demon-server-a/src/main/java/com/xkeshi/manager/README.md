项目简介:以下为基于akka实现的文件服务器 异步的导入导出功能=====================
本例子使用两服务器  server-a  server-b  以下简称 a b
服务器所拥有的角色/群组
a   做为业务服务器  下发文件导入  和导出任务(service)
b   可以执行导入  导出操作(import/output)

发送消息时候需要指定角色/群组名

整体流程
a收到导入指令-->文件通过fileHandler上传至文件服务器-->发送 同步形势 消息(可以使用dubbo)通知记录任务日志的服务器写入本次任务信息(本例子中任务记录数据源在b)
-->记录完成后 发送 异步形势 导入指令sendMsg 给b(同时 返回 特征id给前端 用于后续查询) --> b收到指令后找到业务对应的dataHanler进行处理(从ftp下载文件,吧文件转换成业务相关的对象,进行数据库的插入)
--> 数据导入完成后更新任务信息记录里面的结果与状态--> a通过上一步的返回的特征id 可以查询到导入的状态

a收到导出指令-->同上 同步发送记录任务消息 得到特征id-->将数据库相关查询参数组合成导出消息 异步发送给b-->b收到指令后找到对应handler(查询数据库,将数据组合成写入文件的格式,写入文件并上传到ftp 返回路径更新到任务数据库)
--> a 通过特征id 可以查询到导出状态 已经相关文件信息 进行下载



a,b项目 均引入task-common

使用说明==================================================
a端:
配置文件: FtpHandlerImpl(文件上传下载操作类),AkkaProcessHandler(akka注解模式开启)
1、发送消息是指定ServiceSupport,并设置相关参数 (导入消息对象ImportTask,导出消息对象OutPutTask,发送消息方法参考ApplicationControll)

b端
配置文件: FtpHandlerImpl(文件上传下载操作类),AkkaProcessHandler(akka注解模式开启),TaskProcessHandler(数据处理handler扫描),DataHandlerManagerImpl(数据处理handler策略类)
新增业务流程:
1、common里面的ServiceSupport枚举新增一个业务类型
2、到datahandlers目录下,定义业务handler 继承dataHandler  实现相关的方法。并设置matchServiceSupoort 对应上一步的新增业务类型


注======================================================
1、导出操作中 可以通过outputTask里面的参数来配置 当数据过大而造成的文件切割 是否需要打包
2、@AskActorRef 中askHandle 可以设置消息失败的重试机制
3、目前fileHandler使用ftp实现,可以通过实现handler接口来使用其他文件服务器
4、增加服务器是需要修改application.conf里面的akka端口号,并且设置合适的role名称(根据业务来分类,可以多个 类似group含义) 启动即可参与到集群中


