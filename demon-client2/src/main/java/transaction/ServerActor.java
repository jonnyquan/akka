package transaction;

import akka.actor.ActorRef;
import akka.actors.AbstractActor;
import akka.msg.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by ruancl@xkeshi.com on 2016/12/29.
 */
public class ServerActor extends AbstractActor {


    int num;

    List<ActorRef> actors;

    Transacation transaction;

    CountDownLatch countDownLatch;

    public ServerActor(int num, CountDownLatch countDownLatch, Transacation transaction) {
        this.actors = new ArrayList<>();
        this.num = num;
        this.transaction = transaction;
        this.countDownLatch = countDownLatch;
    }

    @Override
    protected void handleMsg(Message message) {
        if (message.getContent().equals("rollback")) {
            actors.add(getSender());
            //已有list统一发送 rollback
            actors.forEach(o -> o.tell(new Message("rollBack"), getSelf()));
            if (actors.size() == num) {
                transaction.rollback();
                countDownLatch.countDown();
            }
            getContext().become(o -> {
                getSender().tell(new Message("rollBack"), getSelf());
                if (actors.size() == num) {
                    transaction.rollback();
                    countDownLatch.countDown();
                }
            });
        } else {
            actors.add(getSender());
            if (actors.size() == num) {
                //统一发送over
                actors.forEach(o -> o.tell(new Message("over"), getSelf()));
                transaction.commit();
                countDownLatch.countDown();
            }
        }

    }
}
