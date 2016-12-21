package akka;

import akka.annotations.ActorRef;
import akka.core.Akka;
import akka.core.Sender;
import akka.params.AskProcessHandler;
import akka.params.DefaultAskProcessHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import java.lang.reflect.Field;

/**
 * Created by ruancl@xkeshi.com on 16/11/10.
 *
 * 将该对象配置入spring容器 akka随spring启动
 */
public class AkkaProcessHandler extends InstantiationAwareBeanPostProcessorAdapter {

    private Akka akka;

    public AkkaProcessHandler() {
        this.akka = AkkaInit.InitAkka();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ActorRef actorRef = field.getAnnotation(ActorRef.class);
            if (actorRef != null) {
                autoWireActorRef(bean, field, actorRef);
            }
        }

        return bean;
    }


    private void autoWireActorRef(Object bean, Field field, ActorRef actorRef) {
        try {
            if(field.getDeclaringClass()!=Sender.class){
                throw new IllegalAccessError("对象类型错误 必须是sender接口");
            }
            field.setAccessible(true);
            AskProcessHandler handle = null;
            Class handleClazz = actorRef.askHandle();
            if (handleClazz == AskProcessHandler.class) {
                handle = new DefaultAskProcessHandler();
            } else {
                handle = (AskProcessHandler) handleClazz.newInstance();
            }
            Sender sender;
            switch (actorRef.request_type()){
                case TELL:
                    sender = akka.createTellSender(actorRef.name(),actorRef.routerStrategy());
                    break;
                case ASK:
                    sender = akka.createAskSender(actorRef.name(),handle,actorRef.routerStrategy());
                    break;
                default:
                    sender = akka.createTellSender(actorRef.name(),actorRef.routerStrategy());
                    break;
            }
            field.set(bean, sender);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


}
