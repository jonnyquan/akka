package akka.core;

import akka.core.Sender;
import akka.enums.RouterStrategy;
import akka.params.AskProcessHandler;

/**
 * Created by ruancl@xkeshi.com on 2016/12/21.
 */
public interface Akka {


    /**
     * 全双工-----------------------------
     */

    Sender createAskSender(String name, RouterStrategy routerStrategy);

    /**
     *
     * @param name
     * @param askProcessHandler  ask模式下面自定义处理器
     * @param routerStrategy   路由模式
     * @return
     */
    Sender createAskSender(String name, AskProcessHandler<?, ?> askProcessHandler, RouterStrategy routerStrategy);

    Sender createAskSender(String name);

    /**
     * 单工
     */

    Sender createTellSender(String name,RouterStrategy routerStrategy);


    Sender createTellSender(String name);
}
