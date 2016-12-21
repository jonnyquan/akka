package akka.core;

import akka.enums.RequestType;
import akka.enums.RouterStrategy;
import akka.enums.TransferType;
import akka.msg.Message;
import akka.params.AskProcessHandler;


/**
 * Created by ruancl@xkeshi.com on 16/11/17.
 *
 *  消息发送接口实现
 */
public class MsgSenderImpl implements MsgSender {

    private Sender askWrapper;

    private Sender tellWrapper;


    public MsgSenderImpl(String name, AkkaSystem akkaSystem, AskProcessHandler askProcessHandler, RouterStrategy routerStrategy) {
        this.askWrapper = akkaSystem.createAskMsgWrapper(name, askProcessHandler,routerStrategy);
        this.tellWrapper = akkaSystem.createTellMsgWrapper(name,routerStrategy);
    }

    /**
     * @param message
     * @return
     */
    @Override
    public Object sendMsg(Message message, RequestType requestType) {
        Sender sender;
        switch (requestType){
            case TELL:
                sender = tellWrapper;
                break;
            case ASK:
                sender = askWrapper;
                break;
            default:
                throw new IllegalArgumentException("未知的发送类型");
        }
        return sender.sendMsg(message);
    }
}
