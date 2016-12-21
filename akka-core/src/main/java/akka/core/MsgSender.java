package akka.core;

import akka.enums.RequestType;
import akka.enums.TransferType;
import akka.msg.Message;


/**
 * Created by ruancl@xkeshi.com on 16/11/17.
 *  消息发送使用接口
 */
public interface MsgSender {

    /**
     * @param message
     * @param requestType     消息类型 ack双工  tell单工
     * @return
     */
    Object sendMsg(Message message, RequestType requestType);
}
