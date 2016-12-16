package akka.enter;

import akka.actor.ActorSystem;
import akka.enums.TransferType;
import akka.msg.Message;


/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 */
public class TellSenderWrapper extends AbstractSenderWrapper {


    public TellSenderWrapper(String name, AddressContex addressContex, ActorSystem system) {
        super(name, addressContex, system);
    }

    @Override
    public Object handleMsg(Message message, TransferType ifCluster) {
        getGetters(ifCluster).forEach(o -> o.tell(message, getSender()));
        return null;
    }
}
