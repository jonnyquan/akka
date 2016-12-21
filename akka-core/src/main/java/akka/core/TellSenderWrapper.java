package akka.core;

import akka.actor.ActorSystem;
import akka.enums.TransferType;
import akka.msg.Message;


/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 *
 *   tell模式消息发送
 */
public class TellSenderWrapper extends AbstractSenderWrapper {


    public TellSenderWrapper(String name, AddressContextImpl addressContextImpl, ActorSystem system) {
        super(name, addressContextImpl, system);
    }

    @Override
    public Object handleMsg(Message message, TransferType ifCluster) {
        getGetters(ifCluster).forEach(o -> o.tell(message, getSender()));
        return null;
    }
}
