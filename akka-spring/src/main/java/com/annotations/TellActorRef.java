package com.annotations;

import akka.enums.RouterGroup;

import java.lang.annotation.*;

/**
 * Created by ruancl@xkeshi.com on 2017/1/9.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TellActorRef {

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
     * 路由策略
     */
    RouterGroup routerStrategy() default RouterGroup.RANDOM;
}
