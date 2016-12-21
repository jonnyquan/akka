package akka.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.enums.RouterStrategy;
import akka.enums.TransferType;
import akka.msg.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 * 抽象(集群 与路由)发消息类
 */
public abstract class AbstractSenderWrapper implements Sender{

    private String gettersKey;

    private RouterStrategy routerStrategy;


    protected AbstractSenderWrapper(String gettersKey,RouterStrategy routerStrategy) {
        this.gettersKey = gettersKey;
        this.routerStrategy = routerStrategy;
    }


    /**
     * 每次消息发送 都会去addressContext获取相应的接收方 actorRef
     * @return
     */
    protected List<ActorRef> getGetters() {
        return AddressStrategy.getReceivers(this.gettersKey,routerStrategy);
    }



    /**
     * @param message
     * @return
     */
    @Override
    public Object sendMsg(Message message) {
        return handleMsg(message);
    }


    protected abstract Object handleMsg(Message message);
}
