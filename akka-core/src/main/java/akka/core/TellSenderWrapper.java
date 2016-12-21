package akka.core;

import akka.actor.ActorSystem;
import akka.actor.Nobody;
import akka.enums.RouterStrategy;
import akka.enums.TransferType;
import akka.msg.Message;


/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 *
 *   tell模式消息发送
 */
public class TellSenderWrapper extends AbstractSenderWrapper {


    public TellSenderWrapper(String name,RouterStrategy routerStrategy) {
        super(name,routerStrategy);
    }

    @Override
    public Object handleMsg(Message message) {
        getGetters().forEach(o -> o.tell(message, null));
        return null;
    }
}
