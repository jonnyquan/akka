package akka.core;

import akka.actor.ActorRef;
import akka.cluster.routing.ClusterRouterGroup;
import akka.enums.RouterGroup;
import akka.params.AskResponseResolver;
import akka.params.RegisterWrapper;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 */
public interface Akka {

    /**
     * actor注册
     *
     * @param registerWrapper
     * @return
     */
    Akka register(RegisterWrapper registerWrapper);

    ActorRef registerRouterActor(ClusterRouterGroup clusterRouterGroup);

    ActorRef registerActor(RegisterWrapper registerWrapper);

    /**
     * 全双工-----------------------------
     * @param group  对应配置文件application.conf里面的roles
     * @param name
     * @param askResponseResolver ask模式下面自定义处理器(建议继承AskProcessHandlerAdapt,也可自己实现接口)
     * @param routerGroup       路由模式
     *                          默认使用 DefaultAskProcessHandler 以及 RouterGroup.RANDOM 负载均衡
     * @return
     */
    AskSender createAskSender(String group, String name, AskResponseResolver askResponseResolver, RouterGroup routerGroup, long timeOut);

    AskSender createAskSender(String group,String name);

    /**
     * 单工
     * 默认使用  RouterGroup.RANDOM 负载均衡
     */

    TellSender createTellSender(String group,String name, RouterGroup routerGroup);


    TellSender createTellSender(String group,String name);

}
