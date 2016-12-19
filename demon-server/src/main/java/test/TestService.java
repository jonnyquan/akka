package test;

import akka.annotations.ActorRef;
import akka.core.MsgSender;
import akka.enums.RequestType;
import akka.enums.TransferType;
import akka.msg.Message;
import akka.params.DefaultAskProcessHandler;
import org.springframework.stereotype.Component;

/**
 * Created by ruancl@xkeshi.com on 16/11/17.
 */
@Component
public class TestService {

    @ActorRef(name = "test", askHandle = DefaultAskProcessHandler.class)
    private MsgSender msgGun;

    @ActorRef(name = "test2")
    private MsgSender msgGun2;


    public void testMsg() {
        msgGun.sendMsg(new Message("ask 路由"), RequestType.TELL, TransferType.ROUTER);//ask 路由
    }

}
