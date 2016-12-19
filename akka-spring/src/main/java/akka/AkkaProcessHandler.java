package akka;

import akka.annotations.ActorRef;
import akka.core.AkkaSystem;
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

    private AkkaSystem akkaSystem;

    public AkkaProcessHandler() {
        this.akkaSystem = AkkaInit.InitAkkaSystem();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ActorRef actorRef = field.getAnnotation(ActorRef.class);
            if (actorRef != null) {
                checkActorRef(bean, field, actorRef);
            }
        }

        return bean;
    }


    private void checkActorRef(Object bean, Field field, ActorRef actorRef) {
        try {
            field.setAccessible(true);
            AskProcessHandler handle = null;
            Class handleClazz = actorRef.askHandle();
            if (handleClazz == AskProcessHandler.class) {
                handle = new DefaultAskProcessHandler();
            } else {
                handle = (AskProcessHandler) handleClazz.newInstance();
            }
            field.set(bean, akkaSystem.createMsgGun(actorRef.name(), handle));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


}
