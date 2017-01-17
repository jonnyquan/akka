package akka.actors;

import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 2017/1/6.
 */
public interface Reply {
    void reply(Message message);
}
