package akka.addrstrategy;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.enums.RouterGroup;

import java.util.List;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 */
public interface AddressContext {

    /**
     *
     * @param path
     * @param routerGroup 可选参数 路由模式必选
     */
    void initReceivers(String path,RouterGroup routerGroup);

    List<ActorRef> getReceivers(String path);
}
