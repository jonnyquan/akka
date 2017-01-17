package akka.msg;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ruancl@xkeshi.com on 16/10/19.
 * 消息封装类
 */
public class Message implements Serializable {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private Object content;

    private MessageStatus messageStatus = MessageStatus.OK;

    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(MessageStatus messageStatus) {
        this.messageStatus = messageStatus;
    }

    public Message(Object content) {
        this.content = content;
    }

    public int retry(){
        return atomicInteger.addAndGet(1);
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public static Message emptyMessage(){
        return new Message(null);
    }

    public void reflushSelf(Message message){
        this.atomicInteger = message.atomicInteger;
        this.content = message.content;
    }
}
