package test;

import akka.anntations.ActorRef;
import akka.enter.MsgSender;
import akka.msg.Message;
import akka.params.DefaultAskHandle;
import org.springframework.stereotype.Component;

/**
 * Created by ruancl@xkeshi.com on 16/11/17.
 */
@Component
public class TestService {

    @ActorRef(name = "test", askHandle = DefaultAskHandle.class)
    private MsgSender msgGun;

    @ActorRef(name = "test2")
    private MsgSender msgGun2;


    public void testMsg() {
        msgGun.sendMsg(new Message("tell 路由"));//tell 路由
        msgGun.sendMsg(new Message("tell 路由"),false,false);//tell 路由
        msgGun.sendMsg(new Message("tell 群发"),false,true);//tell 群发
        msgGun.sendMsg(new Message("ask 群发"),true,true);//ask 群发
        msgGun.sendMsg(new Message("ask 路由"),true,false);//ask 路由
    }

}
