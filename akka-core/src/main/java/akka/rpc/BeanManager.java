package akka.rpc;

import akka.anntations.ActorRef;
import akka.anntations.AkkaRpcRef;
import akka.enter.AkkaInitFactory;
import akka.params.AskHandle;
import akka.params.DefaultAskHandle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by ruancl@xkeshi.com on 16/11/10.
 */
@Service
public class BeanManager extends InstantiationAwareBeanPostProcessorAdapter {

    @Autowired
    private AkkaInitFactory akkaInitFactory;


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        Annotation[] annotations = clazz.getAnnotations();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                Class annotationClazz = annotation.getClass();
                if (annotationClazz == AkkaRpcRef.class) {
                    return akkaInitFactory.getBean(bean.getClass().getInterfaces()[0]);
                }
            }
        }
        //// TODO: 16/11/17 rpc与actorref同时在一个对象里面是否会冲突 ??
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ActorRef actorRef = field.getAnnotation(ActorRef.class);
            AkkaRpcRef akkaRpcRef = field.getAnnotation(AkkaRpcRef.class);
            if (actorRef != null) {
                checkActorRef(bean, field, actorRef);
            }
            if (akkaRpcRef != null) {
                checkAkkaRpcRef(bean, field);
            }
        }

        return bean;
    }

    private void checkAkkaRpcRef(Object bean, Field field) {
        try {
            field.setAccessible(true);
            field.set(bean, akkaInitFactory.getBean(field.getType()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void checkActorRef(Object bean, Field field, ActorRef actorRef) {
        try {
            field.setAccessible(true);
            AskHandle handle = null;
            Class handleClazz = actorRef.askHandle();
            if (handleClazz == AskHandle.class) {
                handle = new DefaultAskHandle();
            } else {
                handle = (AskHandle) handleClazz.newInstance();
            }
            field.set(bean, akkaInitFactory.createMsgGun(actorRef.name(), handle));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


}
