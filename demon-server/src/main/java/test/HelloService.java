package test;

import akka.annotations.ActorRef;
import akka.params.DefaultAskProcessHandler;
import akka.core.Sender;
import akka.enums.RequestType;
import akka.enums.RouterGroup;
import akka.msg.Message;
import org.springframework.stereotype.Component;

/**
 * Created by ruancl@xkeshi.com on 16/11/17.
 */
@Component
public class HelloService {

    @ActorRef(name = "test", request_type = RequestType.ASK, routerStrategy = RouterGroup.BROADCAST, askHandle = DefaultAskProcessHandler.class)
    private Sender sender1;

    @ActorRef(name = "test", request_type = RequestType.ASK, routerStrategy = RouterGroup.RANDOM)
    private Sender sender;


    public void sayHello() {
        sender.sendMsg(new Message("hello guy!"));//ask 路由
    }

}
