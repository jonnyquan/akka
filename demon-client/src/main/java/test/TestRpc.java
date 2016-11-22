package test;

import akka.anntations.AkkaRpc;
import akka.test.Hello;
import org.springframework.stereotype.Component;

/**
 * Created by ruancl@xkeshi.com on 16/11/17.
 */
@Component
@AkkaRpc
public class TestRpc implements Hello {
    @Override
    public String sayHello() {
        return "hello rpc 测试";
    }
}
