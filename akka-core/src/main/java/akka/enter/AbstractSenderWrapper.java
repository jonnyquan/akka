package akka.enter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.msg.Message;
import com.xkeshi.core.utils.CollectionUtils;
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

    protected List<ActorRef> getGetters(Boolean ifCluster) {
        if (!ifCluster) {
            return Arrays.asList(addressContex.getRoutActor(system, gettersKey));
        }
        List<ActorRefMap> maps = addressContex.getActorRefs(gettersKey);
        if (CollectionUtils.isEmpty(maps)) {
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
        return handleMsg(message, false);
    }

    /**
     * @param message
     * @param ifCluster false 默认 路由模式 单条消息发送   true 集群模式 一对多发送
     * @return
     */
    @Override
    public Object sendMsg(Message message, Boolean ifCluster) {
        return handleMsg(message, ifCluster);
    }


    protected abstract Object handleMsg(Message message, Boolean ifCluster);
}
