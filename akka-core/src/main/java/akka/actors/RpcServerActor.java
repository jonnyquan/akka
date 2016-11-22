package akka.actors;

import akka.actor.UntypedActor;
import akka.enter.SpringContextUtil;
import akka.msg.RpcEntity;
import akka.msg.RpcResult;
import akka.test.Hello;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruancl@xkeshi.com on 16/10/11.
 */
public class RpcServerActor extends UntypedActor {

    private SpringContextUtil applicationContextUtil;

    public RpcServerActor(SpringContextUtil applicationContextUtil) {
        this.applicationContextUtil = applicationContextUtil;
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        System.out.println("收到消息:" + o);
        if (o instanceof RpcEntity) {
            RpcEntity rpcEntity = (RpcEntity) o;
            Object[] params = rpcEntity.getParam();
            System.out.println(rpcEntity.getInterfaceName() + " :: " + Hello.class);
            System.out.println("是否一样:" + (rpcEntity.getInterfaceName() == Hello.class));
            System.out.println(applicationContextUtil.getBean(Hello.class).sayHello());
            Object bean = applicationContextUtil.getBean(rpcEntity.getInterfaceName());
            Class<?>[] paramerTypes = new Class<?>[]{};
            List<Class> paramType = new ArrayList<>();
            if (bean != null) {
                Method method = null;
                if (params != null) {
                    for (Object param : params) {
                        paramType.add(param.getClass());
                    }
                    method = bean.getClass().getMethod(rpcEntity.getMethod(), paramType.toArray(paramerTypes));
                } else {
                    method = bean.getClass().getMethod(rpcEntity.getMethod());
                }
                Object result = method.invoke(bean, params);
                if (result != null) {
                    getSender().tell(new RpcResult(result), getSelf());
                }
            }
        } else {
            unhandled(o);
        }
    }
}
