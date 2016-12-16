package akka.enter;

import akka.enums.RequestType;
import akka.enums.TransferType;
import akka.msg.Message;
import akka.params.AskHandle;


/**
 * Created by ruancl@xkeshi.com on 16/11/17.
 */
public class MsgGun implements MsgSender {

    private Sender askWrapper;

    private Sender tellWrapper;


    public MsgGun(String name, AkSystem akSystem, AskHandle askHandle) {
        this.askWrapper = akSystem.createAskMsgWrapper(name, askHandle);
        this.tellWrapper = akSystem.createTellMsgWrapper(name);
    }

    /**
     * @param message
     * @return
     */
    @Override
    public Object sendMsg(Message message, RequestType requestType, TransferType transferType) {
        Sender sender;
        switch (requestType){
            case TELL:
                sender = tellWrapper;
                break;
            case ACK:
                sender = askWrapper;
                break;
            default:
                throw new IllegalArgumentException("未知的发送类型");
        }
        return sender.sendMsg(message,transferType);
    }
}
