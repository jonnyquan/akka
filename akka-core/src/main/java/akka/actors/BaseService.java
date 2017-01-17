package akka.actors;

import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 2017/1/6.
 *  * 自定义actor都来实现该接口

 */
public interface BaseService {
    void handleMsg(Message message,Reply reply);

}
