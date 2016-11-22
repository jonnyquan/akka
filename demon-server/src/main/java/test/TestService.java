package test;

import akka.anntations.ActorRef;
import akka.anntations.AkkaRpcRef;
import akka.enter.MsgSender;
import akka.msg.Message;
import akka.params.DefaultAskHandle;
import akka.test.Hello;
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

    @AkkaRpcRef
    private Hello hello;

    public void testMsg() {
        msgGun.sendMsg(new Message("hello"));//tell 路由
       /* msgGun.sendMsg(new Message("hello"),false,false);//tell 路由
        msgGun.sendMsg(new Message("hello"),false,true);//tell 群发
        msgGun.sendMsg(new Message("hello"),true,true);//ask 群发
        msgGun.sendMsg(new Message("hello"),true,false);//ask 路由*/
    }

    public void testRpc() {
        System.out.println(hello.sayHello());
    }
}
