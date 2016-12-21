package test;

import akka.actors.AbstractActor;
import akka.anntations.Actor;
import akka.enums.RouterStrategy;
import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 16/11/16.
 */
@Actor(name = "test2", pool = RouterStrategy.ROBIN, number = 5)
public class TestActor extends AbstractActor {

    @Override
    public void handleMsg(Message message) {
        System.out.println("test2消息来了---client2" + Thread.currentThread());
        feedBack(new Message("i got it---client2"));
    }
}
