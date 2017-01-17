package akka.actors;

import akka.actor.UntypedActor;
import akka.msg.Message;
import akka.msg.MessageStatus;

/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 */
public abstract class AbstractActor extends UntypedActor implements Reply{


    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof Message) {
            Message message = (Message) o;
            if(message.getMessageStatus() != MessageStatus.OK){
                throw new IllegalAccessError("消息被拒绝");
            }
            handleMsg(message);
        } else {
            unhandled(o);
        }
    }

    /**
     * 回复消息
     *
     * @param message
     */
    @Override
    public void reply(Message message) {
        getSender().tell(message, getSelf());
    }

    protected abstract void handleMsg(Message message);
}
