package akka.enter;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * Created by ruancl@xkeshi.com on 16/11/17.
 * <p>
 * 此方法还不能用
 */
@Component
// TODO: 16/11/17
public class SpringContextUtil implements BeanFactoryAware {

    private BeanFactory beanFactory;

    public <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
