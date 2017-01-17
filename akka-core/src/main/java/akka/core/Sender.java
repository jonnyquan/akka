package akka.core;

import akka.enums.RouterGroup;
import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 16/12/1.
 * <p>
 * ask和tell模式的消息发送抽象接口
 */
public interface Sender {

    /**
     * @param message
     * @return
     */
    void sendMsg(Message message);

    String getGroupName();

    String getTopicName();

    RouterGroup getRouterGroup();

}
