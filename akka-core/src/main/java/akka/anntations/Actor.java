package akka.anntations;

import akka.enums.PoolType;

import java.lang.annotation.*;

/**
 * Created by ruancl@xkeshi.com on 16/10/19.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Actor {
    String name();

    PoolType pool() default PoolType.ROUBIN;

    int number() default 1;
}
