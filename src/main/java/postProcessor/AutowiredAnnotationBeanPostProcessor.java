package postProcessor;

import annotations.Autowired;
import annotations.Qualifier;
import beansFactories.BeanFactory;


import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * BeanPostProcessor отвечающий за autowired
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    /** Фабрика бинов*/
    private BeanFactory beanFactory;

    public AutowiredAnnotationBeanPostProcessor(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public AutowiredAnnotationBeanPostProcessor() {}

    /**
     * Выполнение postProcessor
     * @param bean - бин
     * @param beanName - имя бина
     * @param beanFactory - ссылка на фабрику
     * @return - настроенный бин
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName, BeanFactory beanFactory){
        autoWiredConstructor(bean, beanFactory);
        autoWiredField(bean, beanFactory);
        autoWiredMethods(bean, beanFactory);
        return bean;
    }

    /**
     * autowired для конструктора
     * @param bean - имя бина
     * @param beanFactory - ссылка на фабрику
     * @return - бин
     */
    private Object autoWiredConstructor(Object bean,BeanFactory beanFactory) {
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
                                    try {
                                        field.set(bean, beanFactory.getBean(parameter.getAnnotation(Qualifier.class).value()));
                                    } catch (IllegalAccessException e){
                                        System.out.println("autoWiredConstructor exception in field.set with Qualifier for class "+ bean.getClass().getSimpleName());
                                    }

                                    return bean;
                                }
                                try {
                                    field.set(bean, beanFactory.getBean(paramType));
                                } catch(IllegalAccessException e){
                                    System.out.println("autoWiredConstructor exception in field.set for class " + bean.getClass().getSimpleName());
                                }
                            }
                        }
                    }
                }
            }
        }
        return bean;
    }
    /**
     * autowired для методов
     * @param bean - имя бина
     * @param beanFactory - ссылка на фабрику
     * @return - бин
     */
    private Object autoWiredMethods(Object bean,BeanFactory beanFactory){
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Autowired.class)) {
                method.setAccessible(true);
                Class[] paramTypes = method.getParameterTypes();
                for (Class paramType : paramTypes) {
                    for (Field field : bean.getClass().getDeclaredFields()){
                        if (field.getType().equals(paramType)){
                            field.setAccessible(true);
                            Parameter [] parameters = method.getParameters();
                            for (Parameter parameter:parameters) {
                                if (parameter.isAnnotationPresent(Qualifier.class)) {
                                    try {
                                        field.set(bean, beanFactory.getBean(parameter.getAnnotation(Qualifier.class).value()));
                                    } catch (IllegalAccessException e){
                                        System.out.println("autoWiredMethods exception in field.set with Qualifier for class" + bean.getClass().getSimpleName());
                                    }
                                    return bean;
                                }
                                try {
                                    field.set(bean, beanFactory.getBean(paramType));
                                } catch(IllegalAccessException e){
                                    System.out.println("autoWiredMethods exception in field.set for class" + bean.getClass().getSimpleName());
                                }
                            }
                        }
                    }
                }
            }
        }
        return bean;
    }
    /**
     * autowired для полей
     * @param bean - имя бина
     * @param beanFactory - ссылка на фабрику
     * @return - бин
     */
    private Object autoWiredField(Object bean,BeanFactory beanFactory){
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Autowired.class)) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Qualifier.class)) {
                    try {
                        field.set(bean, beanFactory.getBean(field.getAnnotation(Qualifier.class).value()));
                        }catch(IllegalAccessException e){
                            System.out.println("autoWiredField exception in field.set with Qualifier for class" + bean.getClass().getSimpleName());
                        }
                        return bean;
                }
                try {
                    field.set(bean, beanFactory.getBean(field.getType()));
                    }catch (IllegalAccessException e){
                        System.out.println("autoWiredMethods exception in field.set for class" + bean.getClass().getSimpleName());
                    }
            }
        }
        return bean;
    }
}
