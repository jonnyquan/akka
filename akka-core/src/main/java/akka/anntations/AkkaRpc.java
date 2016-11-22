package akka.anntations;

import java.lang.annotation.*;

/**
 * Created by ruancl@xkeshi.com on 16/10/19.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AkkaRpc {
}
