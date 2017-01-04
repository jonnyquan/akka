package transaction;

import akka.AkkaProcessHandler;
import akka.params.RegisterBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Created by ruancl@xkeshi.com on 2016/12/29.
 */
@Component
public class TransactionService {


    @Autowired
    private AkkaProcessHandler akkaProcessHandler;

    public void needDisTransaction(){
        //事务开始  生成一个actor 并且生成一个事务唯一id
        int tid = 110;

        //调用其他服务器的服务A(唯一id作为参数 以供服务A 以该id作为父事务id )
        try {
            Thread.sleep(1000l);
            System.out.println("执行业务A");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //代码执行到最后  请求事务中心的节点actor  要求收集所有服务节点的事务状态 并ask 发送提交确认消息  不成功则发送回滚消息
       /* akkaProcessHandler.getAkka().register(new RegisterBean(ServerActor.class, "F" + tid, 1,countDownLatch, new Transacation() {
            @Override
            public void commit() {
                System.out.println("commit");
            }

            @Override
            public void rollback() {
                System.out.println("rollback");
            }
        }));*/

    }
}
