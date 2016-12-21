package akka.core;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
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

    private static final Logger logger = LoggerFactory.getLogger(AbstractSenderWrapper.class);

    private ActorRef sender;

    private String gettersKey;

    private ActorSystem system;

    private AddressContextImpl addressContextImpl;


    protected AbstractSenderWrapper(String gettersKey, AddressContextImpl addressContextImpl, ActorSystem system) {
        this.sender = addressContextImpl.getSender(system, gettersKey);
        this.gettersKey = gettersKey;
        this.system = system;
        this.addressContextImpl = addressContextImpl;
    }


    protected ActorRef getSender() {
        return sender;
    }

    /**
     * 每次消息发送 都会去addressContext获取相应的接收方 actorRef
     * @param transferType
     * @return
     */
    protected List<ActorRef> getGetters(TransferType transferType) {
        return addressContextImpl.getReceivers(transferType,this.gettersKey,system);
    }

    protected ActorSystem getSystem() {
        return system;
    }


    /**
     * @param message
     * @return
     */
    @Override
    public Object sendMsg(Message message, TransferType transferType) {
        return handleMsg(message, transferType);
    }


    protected abstract Object handleMsg(Message message, TransferType transferType);
}
