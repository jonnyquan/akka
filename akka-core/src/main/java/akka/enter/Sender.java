package akka.enter;

import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 16/12/1.
 */
public interface Sender {

    Object sendMsg(Message message);

    /**
     * @param message
     * @param ifCluster false 默认 路由模式 单条消息发送   true 集群模式 一对多发送
     * @return
     */
    Object sendMsg(Message message, Boolean ifCluster);

}
