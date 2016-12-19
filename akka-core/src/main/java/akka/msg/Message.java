package akka.msg;

import java.io.Serializable;

/**
 * Created by ruancl@xkeshi.com on 16/10/19.
 * 消息封装类
 */
public class Message implements Serializable {


    private Object content;

    public Message(Object content) {
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
