package com.xkeshi.task.services.compnents;


import com.xkeshi.task.enums.ServiceSupport;
import com.xkeshi.task.handlers.AbstractDataProcessor;
import com.xkeshi.task.handlers.DataProcessor;
import com.xkeshi.task.handlers.DataProcessorLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ruancl@xkeshi.com on 16/11/10.
 * <p>
 * 将该对象配置入spring容器 扫描自定义的数据处理器
 */
@Component
public class DataProcessorLocatorInitializer extends InstantiationAwareBeanPostProcessorAdapter implements DataProcessorLocator {

    private static final Logger logger = LoggerFactory.getLogger(DataProcessorLocatorInitializer.class);


    private Map<ServiceSupport,AbstractDataProcessor> map;

    public DataProcessorLocatorInitializer() {
        this.map = new HashMap<>();
    }

    @Override
    public void registHandler(AbstractDataProcessor abstractDataHandler){
        ServiceSupport serviceSupport = abstractDataHandler.matchServiceSupport();
        if(serviceSupport == null){
            throw new NullPointerException("请设置该处理类的ServiceSupport:"+ abstractDataHandler.getClass());
        }
        map.put(serviceSupport, abstractDataHandler);
    }

    @Override
    public DataProcessor locationHandler(ServiceSupport serviceSupport) {
        return map.get(serviceSupport);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        if(clazz.getSuperclass() == AbstractDataProcessor.class){
            registHandler((AbstractDataProcessor) bean);
            logger.info("注册数据处理器:"+beanName);
        }
        return bean;
    }


}