package akka.anntations;

import java.lang.annotation.*;

/**
 * Created by ruancl@xkeshi.com on 16/11/10.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AkkaRpcRef {
}
