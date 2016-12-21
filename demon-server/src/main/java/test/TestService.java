package test;

import akka.annotations.ActorRef;
import akka.core.MsgSender;
import akka.core.Sender;
import akka.enums.RequestType;
import akka.enums.RouterStrategy;
import akka.enums.TransferType;
import akka.msg.Message;
import akka.params.DefaultAskProcessHandler;
import org.springframework.stereotype.Component;

/**
 * Created by ruancl@xkeshi.com on 16/11/17.
 */
@Component
public class TestService {

    @ActorRef(name = "test", request_type = RequestType.ASK,routerStrategy = RouterStrategy.BROADCAST,askHandle = DefaultAskProcessHandler.class)
    private Sender sender1;

    @ActorRef(name = "test2")
    private Sender sender;


    public void testMsg() {
        sender.sendMsg(new Message("ask 路由"));//ask 路由
    }

}
