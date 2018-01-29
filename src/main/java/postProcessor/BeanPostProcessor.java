package postProcessor;

import beansFactories.BeanFactory;

/**
 * BeanPostProcessor по настройке бинов
 */
public interface BeanPostProcessor {
    /**
     * Метод настройки бинов
     * @param bean - бин
     * @param beanName - имя бина
     * @param beanFactory - ссылка на фабрику
     * @return - настроенный бин
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName, BeanFactory beanFactory){
        return bean;
    }
}