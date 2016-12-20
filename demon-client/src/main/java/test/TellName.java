package test;

import akka.actors.AbstractActor;
import akka.anntations.Actor;
import akka.enums.PoolStrategy;
import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 16/11/16.
 */
@Actor(name = "test", pool = PoolStrategy.ROBIN, number = 5)
public class TellName extends AbstractActor {


    @Override
    public void handleMsg(Message message) {
        System.out.println("test1消息来了---client2" + Thread.currentThread());
        feedBack(new Message("i got it--client2"));
    }
}
