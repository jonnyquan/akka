package akka.anntations;

import akka.enums.PoolStrategy;

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
     * @return
     */
    PoolStrategy pool() default PoolStrategy.ROBIN;

    /**
     * 对第二个参数的补充数量
     * @return
     */
    int number() default 1;
}
