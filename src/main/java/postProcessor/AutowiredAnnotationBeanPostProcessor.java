package postProcessor;

import annotations.Autowired;
import annotations.Qualifier;
import beansFactories.BeanFactory;


import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    BeanFactory beanFactory;

    public AutowiredAnnotationBeanPostProcessor(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public AutowiredAnnotationBeanPostProcessor() {
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName, BeanFactory beanFactory) throws Exception {
        autoWiredConstructor(bean, beanFactory);
        autoWiredField(bean, beanFactory);
        return bean;
    }

    private Object autoWiredConstructor(Object bean,BeanFactory beanFactory) throws Exception {
        for (Constructor constructor : bean.getClass().getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(Autowired.class)) {
                constructor.setAccessible(true);
                Class[] paramTypes = constructor.getParameterTypes();
                for (Class paramType : paramTypes) {
                    for (Field field : bean.getClass().getDeclaredFields()){
                        if (field.getType().equals(paramType)){
                            field.setAccessible(true);
                            Parameter [] parameters = constructor.getParameters();
                            for (Parameter parameter:parameters) {
                                if (parameter.isAnnotationPresent(Qualifier.class)) {
                                    field.set(bean, beanFactory.getBean(parameter.getAnnotation(Qualifier.class).value()));
                                    return bean;
                                }
                                field.set(bean, beanFactory.getBean(paramType));
                            }
                        }
                    }
                }
            }


        }
        return bean;
    }
        private Object autoWiredField(Object bean,BeanFactory beanFactory) throws Exception {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                    if (field.isAnnotationPresent(Qualifier.class)) {
                        field.set(bean,beanFactory.getBean(field.getAnnotation(Qualifier.class).value()));
                        return bean;
                    }
                    field.set(bean, beanFactory.getBean(field.getType()));
            }
        }
        return bean;
    }
}
