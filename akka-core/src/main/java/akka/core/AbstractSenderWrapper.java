package akka.core;

import akka.actor.ActorRef;
import akka.actor.Address;
import akka.balancestrategy.LoadBalanceStrategy;
import akka.enums.RouterGroup;
import akka.msg.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ruancl@xkeshi.com on 16/10/12.
 * 抽象(集群 与路由)发消息类
 */
public abstract class AbstractSenderWrapper implements Sender{

    private String gettersKey;

    private RouterGroup routerGroup;

    private AbstractAkkaSystem akkaSystem;

    private LoadBalanceStrategy loadBalanceStrategy;


    protected AbstractSenderWrapper(String gettersKey,RouterGroup routerGroup,AbstractAkkaSystem akkaSystem) {
        this.gettersKey = gettersKey;
        this.routerGroup = routerGroup;
        this.akkaSystem = akkaSystem;
        final AddressStrategy addressStrategy = akkaSystem.getAddressStrategy();
        //地址预加载
        loadBalanceStrategy = new LoadBalanceStrategy(addressStrategy.prepareLoadAdd(gettersKey, routerGroup));
        //为策略加入监听
        addressStrategy.addSubcribe(loadBalanceStrategy);
    }


    /**
     * 每次消息发送 都会去addressContext获取相应的接收方 actorRef
     * @return
     */
    protected List<ActorRef> getGetters() {
        Map<Address,ActorRef> refs = akkaSystem.getAddressStrategy().getReceivers(this.gettersKey);
        if(routerGroup == RouterGroup.BROADCAST){
            return refs.values().stream().collect(Collectors.toList());
        }
        return Arrays.asList(loadBalanceStrategy.router(refs,routerGroup));
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
