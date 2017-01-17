package com.annotations;

import akka.enums.RouterPool;

import java.lang.annotation.*;

/**
 * Created by ruancl@xkeshi.com on 16/10/19.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Actor {

    String name();

    /**
     * rout模式下的路由策略
     *
     * @return
     */
    RouterPool pool() default RouterPool.ROBIN;

    /**
     * 对第二个参数的补充数量
     *
     * @return
     */
    int number() default 1;
}
