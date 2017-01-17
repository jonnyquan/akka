package akka.actors;

import akka.msg.Message;

/**
 * Created by ruancl@xkeshi.com on 2017/1/6.
 */
public class BaseActor extends AbstractActor{

    private BaseService baseService;

    public BaseActor(BaseService baseService) {
        this.baseService = baseService;
    }

    @Override
    protected void handleMsg(Message message) {
        baseService.handleMsg(message,this);
    }
}
