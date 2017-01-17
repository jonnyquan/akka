package com;

import akka.actors.AbstractAkkaService;
import akka.actors.BaseService;
import com.annotations.Actor;
import com.annotations.AskActorRef;
import com.annotations.TellActorRef;
import akka.core.Akka;
import akka.core.AskSender;
import akka.core.TellSender;
import akka.main.AkkaMain;
import akka.params.RegisterWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;

import java.lang.reflect.Field;

/**
 * Created by ruancl@xkeshi.com on 16/11/10.
 * <p>
 * 将该对象配置入spring容器 akka随spring启动
 */
public class AkkaProcessHandler extends InstantiationAwareBeanPostProcessorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AkkaProcessHandler.class);

    private Akka akka;

    public AkkaProcessHandler() {
        this.akka = AkkaMain.initAkka();
    }

    public Akka getAkka() {
        return akka;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        Actor actor = (Actor) clazz.getAnnotation(Actor.class);
        if (actor != null) {
            if (bean instanceof AbstractAkkaService) {
                akka.register(new RegisterWrapper(actor.name(), actor.pool().getPool(actor.number()), (BaseService) bean));
            }else{
                logger.error("{}作为actor必须要继承AbstractAkkaService",clazz);
                throw new IllegalAccessError(clazz+"作为actor必须要继承AbstractAkkaService");
            }
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                AskActorRef askActorRef = field.getAnnotation(AskActorRef.class);
                if (askActorRef != null) {
                    if (field.getType() != AskSender.class) {
                        logger.error("{}对象类型错误 必须是AskSender接口",field.getName());
                        throw new IllegalAccessError(field.getName()+"对象类型错误 必须是AskSender接口");
                    }
                    field.setAccessible(true);
                    field.set(bean, akka.createAskSender(askActorRef.group(), askActorRef.name(), askActorRef.askHandle().newInstance(), askActorRef.routerStrategy(),askActorRef.timeOut()));
                }

                TellActorRef tellActorRef = field.getAnnotation(TellActorRef.class);
                if(tellActorRef !=null){
                    if (field.getType() != TellSender.class) {
                        logger.error("对象类型错误 必须是TellSender接口");
                        throw new IllegalAccessError("对象类型错误 必须是TellSender接口");
                    }
                    field.setAccessible(true);
                    field.set(bean,akka.createTellSender(tellActorRef.group(),tellActorRef.name(),tellActorRef.routerStrategy()));
                }
            } catch (InstantiationException e) {
                logger.error("对象初始化异常");
            } catch (IllegalAccessException e) {
                logger.error("没有字段设置权限");
            }

        }
        return bean;
    }


}