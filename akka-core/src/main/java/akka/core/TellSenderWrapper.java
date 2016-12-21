package akka.core;

import akka.enums.RouterGroup;
import akka.msg.Message;


/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 *
 *   tell模式消息发送
 */
public class TellSenderWrapper extends AbstractSenderWrapper {


    public TellSenderWrapper(String name,RouterGroup routerGroup) {
        super(name, routerGroup);
    }

    @Override
    public Object handleMsg(Message message) {
        getGetters().forEach(o -> o.tell(message, null));
        return null;
    }
}
