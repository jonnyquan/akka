package akka.enter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.enums.TransferType;
import akka.msg.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 */
public abstract class AbstractSenderWrapper implements Sender{

    private static final Logger logger = LoggerFactory.getLogger(AbstractSenderWrapper.class);


    private ActorRef sender;

    private String gettersKey;

    private ActorSystem system;

    private AddressContex addressContex;


    protected AbstractSenderWrapper(String gettersKey, AddressContex addressContex, ActorSystem system) {
        this.sender = addressContex.getSender(system, gettersKey);
        this.gettersKey = gettersKey;
        this.system = system;
        this.addressContex = addressContex;
    }


    protected ActorRef getSender() {
        return sender;
    }

    protected List<ActorRef> getGetters(TransferType transferType) {
        if (transferType == TransferType.ROUTER) {
            return Arrays.asList(addressContex.getRoutActor(system, gettersKey));
        }
        List<ActorRefMap> maps = addressContex.getActorRefs(gettersKey);
        if (maps==null || maps.size()==0) {
            System.out.println("暂无可用客户端接收消息");
            logger.info("暂无可用客户端接收消息");
            return null;
        }
        return maps.stream().map(ActorRefMap::getV).collect(Collectors.toList());
    }

    protected ActorSystem getSystem() {
        return system;
    }

    @Override
    public Object sendMsg(Message message) {
        return handleMsg(message, TransferType.ROUTER);
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
