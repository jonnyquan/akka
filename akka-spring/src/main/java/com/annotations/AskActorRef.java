package com.annotations;

import akka.enums.RouterGroup;
import akka.params.AskResponseResolver;
import akka.params.DefaultAskResponseResolver;

import java.lang.annotation.*;

/**
 * Created by ruancl@xkeshi.com on 2017/1/9.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AskActorRef {

    /**
     * 所属群组名称
     * @return
     */
    String group();

    /**
     * name 必须对应收消息短的 @actor name()
     *
     * @return
     */
    String name();

    /**
     * 响应超时时间 /毫秒
     * @return
     */
    long timeOut() default 20000l;

    /**
     * 路由策略
     */
    RouterGroup routerStrategy() default RouterGroup.RANDOM;

    /**
     * 支持自定义ask模式下面的handle  暂不支持带参构造函数  如需要带参数 请手动调用 @AkkaInitFactory.createMsgGun(name,handle)
     * tell模式没有作用
     *
     * @return
     */
    Class<? extends AskResponseResolver> askHandle() default DefaultAskResponseResolver.class;
}
