package postProcessor;

import beansFactories.BeanFactory;

public interface BeanPostProcessor {
    default Object postProcessBeforeInitialization(Object bean, String beanName, BeanFactory beanFactory) throws Exception {
        return bean;
    }

    /*default Object postProcessAfterInitialization(Object bean, String beanName)  {
        return bean;
    }*/
}