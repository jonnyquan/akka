package akka.rpc;


import akka.actor.ActorRef;
import akka.msg.RpcEntity;
import akka.msg.RpcResult;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Created by ruancl@xkeshi.com on 16/10/11.
 * 调用方 调代理类
 */
public class ProxyIntecept implements MethodInterceptor {


    private final Long timeOut = 3000l;

    private Class clazz;

    private ActorRef rpcActor;

    public ProxyIntecept(Class clazz, ActorRef rpcActor) {
        this.clazz = clazz;
        this.rpcActor = rpcActor;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        RpcEntity rpcEntity = new RpcEntity(clazz, method.getName(), objects);
        FiniteDuration finiteDuration = Duration.create(timeOut, TimeUnit.MILLISECONDS);
        final Timeout timeout = new Timeout(finiteDuration);
        final Future future = Patterns.ask(rpcActor, rpcEntity, timeout);

        RpcResult res = (RpcResult) Await.result(future, finiteDuration);

        return res.getO();
    }
}
