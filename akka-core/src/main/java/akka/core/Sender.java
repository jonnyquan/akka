package akka.core;

import akka.enums.TransferType;
import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 16/12/1.
 *
 *  ask和tell模式的消息发送抽象接口
 */
public interface Sender {

    /**
     * @param message
     * @param transferType  默认 路由模式 单条消息发送    集群模式 一对多发送
     * @return
     */
    Object sendMsg(Message message, TransferType transferType);

}
