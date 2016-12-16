package akka.enter;

import akka.enums.RequestType;
import akka.enums.TransferType;
import akka.msg.Message;


/**
 * Created by ruancl@xkeshi.com on 16/11/17.
 */
public interface MsgSender {

    /**
     * @param message
     * @param requestType     消息类型 ack双工  tell单工
     * @param transferType broadcast广播  router路由模式单发
     * @return
     */
    Object sendMsg(Message message, RequestType requestType, TransferType transferType);
}
