package akka.core;

import akka.msg.Message;
import akka.params.AskResponseResolver;

/**
 * Created by ruancl@xkeshi.com on 2017/1/9.
 */
public interface AskSender extends Sender{

    Message sendMsgSync(Message message);

    Message sendMsgSync(Message message, AskResponseResolver askResponseResolver);

    void sendMsg(Message message, AskResponseResolver askResponseResolver);



}
