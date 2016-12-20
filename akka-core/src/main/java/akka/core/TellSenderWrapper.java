package akka.core;

import akka.actor.ActorSystem;
import akka.enums.RouterStrategy;
import akka.enums.TransferType;
import akka.msg.Message;


/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 *
 *   tell模式消息发送
 */
public class TellSenderWrapper extends AbstractSenderWrapper {


    public TellSenderWrapper(String name, AddressContext addressContext, ActorSystem system) {
        super(name, addressContext, system);
    }

    @Override
    public Object handleMsg(Message message, RouterStrategy routerStrategy) {
        getGetters(routerStrategy).forEach(o -> o.tell(message, getSender()));
        return null;
    }
}
