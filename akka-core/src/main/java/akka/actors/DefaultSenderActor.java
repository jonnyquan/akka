package akka.actors;

import akka.actor.*;
import akka.core.ActorRefMap;
import akka.core.AddressContextImpl;
import akka.msg.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruancl@xkeshi.com on 16/9/30.
 *
 * 作为通用的消息发送端actor    sender
 * 在初始化时候 addressContex.getActorRefs(path)为空  为集群模式初始化接收端actorRef
 * 从集群地址里面获取到所有集群地址 加上actorName  使用actorSelection方法获取到actorRef并放入AddressContex
 */
public class DefaultSenderActor extends UntypedActor {

    private List<ActorRefMap> actorRefs;



    public DefaultSenderActor(AddressContextImpl addressContextImpl, String path) {
        this.actorRefs = addressContextImpl.getActorRefs(path);
        if (actorRefs == null) {
            actorRefs = new ArrayList<>();
            addressContextImpl.addMap(path, actorRefs);
            List<Address> addresses = addressContextImpl.getAddresses();
            if (addresses.size() == 0) {
                throw new NullPointerException("集群中没有可用地址,集群离线 or 未开启集群监听");
            }
            addresses.forEach(addr ->
                    getContext().actorSelection(String.format("%s/user/%s", addr.toString(), path)).tell(new Identify(addr), getSelf())
            );
        }
    }


    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof Message) {

        } else if (o instanceof ActorIdentity) {
            ActorIdentity identity = (ActorIdentity) o;
            if (identity.correlationId() instanceof Address) {
                Address address = (Address) identity.correlationId();
                ActorRef actorRef = identity.getRef();
                if (actorRef != null) {
                    actorRefs.add(new ActorRefMap(address, actorRef));
                }
            }
        } else {
            unhandled(o);
        }
    }
}
