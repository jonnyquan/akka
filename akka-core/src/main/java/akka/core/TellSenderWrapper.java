package akka.core;

import akka.actor.ActorRef;
import akka.enums.RouterGroup;
import akka.msg.Message;


/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 * <p>
 * tell模式消息发送
 */
public class TellSenderWrapper extends AbstractSenderWrapper implements TellSender {


    public TellSenderWrapper(String group,String name, RouterGroup routerGroup, AbstractAkkaSystemContext akkaSystem) {
        super(group,name, routerGroup, akkaSystem);
    }

    @Override
    public void handleMsg(Message message) {
        getGetters().tell(message, ActorRef.noSender());
    }

}
