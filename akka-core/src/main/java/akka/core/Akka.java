package akka.core;

import akka.enums.RouterGroup;
import akka.params.AskProcessHandler;
import akka.params.RegisterBean;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 */
public interface Akka {

    /**
     * actor注册
     * @param registerBean
     * @return
     */
    Akka register(RegisterBean registerBean);

    /**
     * 全双工-----------------------------
     */

    Sender createAskSender(String name, RouterGroup routerGroup);

    /**
     *
     * @param name
     * @param askProcessHandler  ask模式下面自定义处理器
     * @param routerGroup   路由模式
     * @return
     */
    Sender createAskSender(String name, AskProcessHandler<?, ?> askProcessHandler, RouterGroup routerGroup);

    Sender createAskSender(String name);

    /**
     * 单工
     */

    Sender createTellSender(String name,RouterGroup routerGroup);


    Sender createTellSender(String name);
}
