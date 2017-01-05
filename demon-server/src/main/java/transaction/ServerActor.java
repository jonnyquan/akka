package transaction;

import akka.actor.ActorRef;
import akka.actors.AbstractActor;
import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 2016/12/29.
 */
public class ServerActor extends AbstractActor {


    ActorRef father;
    Transacation transacation;

    public ServerActor(ActorRef father, Transacation transacation) {
        this.father = father;
        this.transacation = transacation;
        father.tell(new Message("commit"), getSelf());
    }

    @Override
    protected void handleMsg(Message message) {
        if (message.getContent().equals("over")) {
            transacation.commit();
        }

        if (message.getContent().equals("rollback")) {
            transacation.rollback();
        }
    }
}
