package akka.anntations;


import akka.params.AskHandle;
import akka.params.DefaultAskHandle;

import java.lang.annotation.*;

/**
 * Created by ruancl@xkeshi.com on 16/11/17.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ActorRef {
    /**
     * name 必须对应收消息短的 @actor name()
     *
     * @return
     */
    String name();

    Class<? extends AskHandle> askHandle() default DefaultAskHandle.class;
}
