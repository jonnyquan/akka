package akka.core;

import akka.actor.ActorRef;
import akka.enums.RouterGroup;
import akka.msg.Message;

import java.util.List;

/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 * 抽象(集群 与路由)发消息类
 */
public abstract class AbstractSenderWrapper implements Sender{

    private String gettersKey;

    private RouterGroup routerGroup;

    private AbstractAkkaSystem akkaSystem;


    protected AbstractSenderWrapper(String gettersKey,RouterGroup routerGroup,AbstractAkkaSystem akkaSystem) {
        this.gettersKey = gettersKey;
        this.routerGroup = routerGroup;
        this.akkaSystem = akkaSystem;
    }


    /**
     * 每次消息发送 都会去addressContext获取相应的接收方 actorRef
     * @return
     */
    protected List<ActorRef> getGetters() {
        return akkaSystem.getAddressStrategy().getReceivers(this.gettersKey, routerGroup);
    }



    /**
     * @param message
     * @return
     */
    @Override
    public Object sendMsg(Message message) {
        return handleMsg(message);
    }


    protected abstract Object handleMsg(Message message);
}
