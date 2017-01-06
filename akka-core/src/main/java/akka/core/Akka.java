package akka.core;

import akka.actor.ActorRef;
import akka.cluster.routing.ClusterRouterGroup;
import akka.enums.RouterGroup;
import akka.params.AskProcessHandler;
import akka.params.RegisterBean;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 */
public interface Akka {

    /**
     * actor注册
     *
     * @param registerBean
     * @return
     */
    Akka register(RegisterBean registerBean);

    ActorRef registerRouterActor(ClusterRouterGroup clusterRouterGroup);

    ActorRef registerActor(RegisterBean registerBean);

    /**
     * 全双工-----------------------------
     * @param group  对应配置文件application.conf里面的roles
     */

    Sender createAskSender(String group,String name, RouterGroup routerGroup);

    /**
     * @param name
     * @param askProcessHandler ask模式下面自定义处理器(建议继承AskProcessHandlerAdapt,也可自己实现接口)
     * @param routerGroup       路由模式
     *                          默认使用 DefaultAskProcessHandler 以及 RouterGroup.RANDOM 负载均衡
     * @return
     */
    Sender createAskSender(String group,String name, AskProcessHandler<?, ?> askProcessHandler, RouterGroup routerGroup);

    Sender createAskSender(String group,String name);

    /**
     * 单工
     * 默认使用  RouterGroup.RANDOM 负载均衡
     */

    Sender createTellSender(String group,String name, RouterGroup routerGroup);


    Sender createTellSender(String group,String name);

}
