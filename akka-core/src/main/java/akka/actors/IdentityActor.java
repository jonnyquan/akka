package akka.actors;

import akka.actor.*;
import akka.core.ActorRefMap;
import akka.params.ActorAddress;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 16/9/30.
 *
 * 作为通用的消息发送端actor    sender
 * 在初始化时候 addressContex.getActorRefs(path)为空  为集群模式初始化接收端actorRef
 * 从集群地址里面获取到所有集群地址 加上actorName  使用actorSelection方法获取到actorRef并放入AddressContex
 */
public class IdentityActor extends UntypedActor {
    private Map<String, List<ActorRefMap>> map;



    public IdentityActor(Map<String, List<ActorRefMap>> map) {
        this.map = map;
    }


    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof ActorIdentity) {
            ActorIdentity identity = (ActorIdentity) o;
            if (identity.correlationId() instanceof ActorAddress) {
                ActorAddress address = (ActorAddress) identity.correlationId();
                ActorRef actorRef = identity.getRef();
                if (actorRef != null) {
                    List<ActorRefMap> refs = map.get(address.getActorName());
                    if(refs == null){
                        refs = new ArrayList<>();
                    }
                    refs.add(new ActorRefMap(address.getAddress(), actorRef));
                }
            }
        } else {
            unhandled(o);
        }
    }
}
