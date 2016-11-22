package akka.enter;

import akka.msg.Message;
import akka.params.AskHandle;


/**
 * Created by ruancl@xkeshi.com on 16/11/17.
 */
public class MsgGun implements MsgSender {

    private MsgSenderWrapper askWrapper;

    private MsgSenderWrapper tellWrapper;


    public MsgGun(String name, AkSystem akSystem, AskHandle askHandle) {
        this.askWrapper = akSystem.createAskMsgWrapper(name, askHandle);
        this.tellWrapper = akSystem.createTellMsgWrapper(name);
    }


    /**
     * 默认使用tell方式的  rout模式
     *
     * @param message
     * @return
     */
    @Override
    public Object sendMsg(Message message) {
        return sendMsg(message, false, false);
    }

    /**
     * @param message
     * @param ifAsk     消息类型 ask  tell
     * @param ifCluster true为群发  false为路由模式单发
     * @return
     */
    @Override
    public Object sendMsg(Message message, Boolean ifAsk, Boolean ifCluster) {
        if (ifAsk) {
            return askWrapper.sendMsg(message, ifCluster);
        } else {
            return tellWrapper.sendMsg(message, ifCluster);
        }
    }
}
