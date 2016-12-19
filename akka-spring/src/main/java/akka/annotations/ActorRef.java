package akka.annotations;


import akka.params.AskProcessHandler;
import akka.params.DefaultAskProcessHandler;

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

    /**
     * 支持自定义ask模式下面的handle  暂不支持带参构造函数  如需要带参数 请手动调用 @AkkaInitFactory.createMsgGun(name,handle)
     * @return
     */
    Class<? extends AskProcessHandler> askHandle() default DefaultAskProcessHandler.class;

}
