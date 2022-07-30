package gaozhi.online.base.component;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * 获取实例
 */
@Component
public class GetBeanHelper implements BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public Object getBean(String beanName) {
        return this.beanFactory.getBean(beanName);
    }

    public <T> T getBean(Class<T> clazz) {
        return this.beanFactory.getBean(clazz);
    }

    public <T> T getBean(String beanName, Class<T> clazz) {
        try {
            return this.beanFactory.getBean(beanName, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
