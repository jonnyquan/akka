package transaction;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.actors.AbstractActor;
import akka.cluster.Cluster;
import akka.cluster.Member;
import akka.core.AbstractAkkaSystem;
import akka.core.Akka;
import akka.core.AskSenderWrapper;
import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import akka.enums.RouterGroup;
import akka.msg.Message;
import akka.params.AskProcessHandler;
import akka.params.DefaultAskProcessHandler;
import scala.collection.Iterator;
import scala.collection.SortedSet;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by ruancl@xkeshi.com on 2016/12/29.
 */
public class ServerActor extends AbstractActor {


    ActorRef father;
    Transacation transacation;

    public ServerActor(ActorRef father,Transacation transacation) {
        this.father = father;
        this.transacation = transacation;
        father.tell(new Message("commit"),getSelf());
    }

    @Override
    protected void handleMsg(Message message) {
        if(message.getContent().equals("over")){
            transacation.commit();
        }

        if(message.getContent().equals("rollback")){
            transacation.rollback();
        }
    }
}
