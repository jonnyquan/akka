package akka.enter;

import akka.msg.Message;


/**
 * Created by ruancl@xkeshi.com on 16/11/17.
 */
public interface MsgSender {

    /**
     * 默认使用tell方式的  rout模式
     *
     * @param message
     * @return
     */
    Object sendMsg(Message message);

    /**
     * @param message
     * @param ifAsk     消息类型 ask  tell
     * @param ifCluster true为群发  false为路由模式单发
     * @return
     */
    Object sendMsg(Message message, Boolean ifAsk, Boolean ifCluster);
}
