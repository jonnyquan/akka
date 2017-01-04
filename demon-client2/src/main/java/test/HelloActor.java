package test;

import akka.actors.AbstractActor;
import akka.anntations.Actor;
import akka.enums.RouterPool;
import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 16/11/16.
 */
@Actor(name = "test", pool = RouterPool.ROBIN, number = 5)
public class HelloActor extends AbstractActor {


    @Override
    public void handleMsg(Message message) {
        System.out.println(message.getContent() + "  : " + Thread.currentThread());
        reply(new Message("你好 我是client2"));
    }
}
