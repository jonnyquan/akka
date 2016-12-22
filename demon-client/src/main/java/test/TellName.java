package test;

import akka.actors.AbstractActor;
import akka.anntations.Actor;
import akka.enums.RouterGroup;
import akka.enums.RouterPool;
import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 16/11/16.
 */
@Actor(name = "test", pool = RouterPool.ROBIN, number = 5)
public class TellName extends AbstractActor {


    @Override
    public void handleMsg(Message message) {
        System.out.println(message.getContent()+":" + Thread.currentThread());
        feedBack(new Message("你好2"));
    }
}
