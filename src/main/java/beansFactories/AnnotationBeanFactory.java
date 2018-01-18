package beansFactories;

import bd.BeanDefinition;
import config.AnnotatedBeanDefinitionReader;
import postProcessor.BeanPostProcessor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationBeanFactory implements BeanFactory{
    private Map<String, Object> beansObject = new HashMap<>();
    private Map<String, Object> beansPostProcessor = new HashMap<>();

    AnnotatedBeanDefinitionReader bdReader;

    public Map<String, Object> getBeansObject() {
        return beansObject;
    }

    public AnnotationBeanFactory(AnnotatedBeanDefinitionReader bdReader) throws Exception {
        this.bdReader = bdReader;
        beansPostProcessor = createBeans(bdReader.getBeanDefinitionsPostProcessor());
        beansObject = createBeans(bdReader.getBeanDefinitions());
        applyPostProcessors();
    }
    /**
     * Создает бины из описания вызвая фабричный метод
     */
    private Object createBean(String beanName,Map<String, BeanDefinition> listBeans) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
          if (listBeans.containsKey(beanName)) {
            Class className = listBeans.get(beanName).getContextBeanClass();
            Method method = className.getDeclaredMethod(listBeans.get(beanName).getFactoryMethodName());
            Object bean = method.invoke(className.newInstance(), null);
            return bean;
        }
        else
            throw new IllegalArgumentException("Bean with the name \""+beanName + "\" doesn't exist");
    }

    private Map<String, Object> createBeans(Map<String, BeanDefinition> listBeans) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Map<String, Object> beans = new HashMap<>();
        for (Map.Entry<String, BeanDefinition> entry : listBeans.entrySet()) {
            beans.put(entry.getKey(),createBean(entry.getKey(),listBeans));
        }
        return beans;
    }
    /**
     * Применяем постпроцессоры к переданному бину
     */
    private void applyPostProcessor(Object bean, String beanName) throws Exception {
        for (Map.Entry<String, Object> entryPostProcessor : beansPostProcessor.entrySet()) {
                BeanPostProcessor postProcessor = (BeanPostProcessor) entryPostProcessor.getValue();
                beansObject.put(beanName,postProcessor.postProcessBeforeInitialization(bean, beanName,this));
            }
        }
    /**
     * Настраиваем бины с помощью постпроцессоров
     */
    private void applyPostProcessors() throws Exception {
        for (Map.Entry<String, Object> entryPostProcessor : beansPostProcessor.entrySet()) {
                BeanPostProcessor postProcessor = (BeanPostProcessor) entryPostProcessor.getValue();
                for (Map.Entry<String, Object> entryBean : beansObject.entrySet())
                    beansObject.put(entryBean.getKey(),postProcessor.postProcessBeforeInitialization(entryBean.getValue(), entryBean.getKey(),this));
        }
    }
    @Override
    public Object getBean(String name) throws Exception {
        if (beansObject.containsKey(name)) {
            if (bdReader.getBeanDefinitions().get(name).getScope().equals("prototype")) {
                createBean(name,bdReader.getBeanDefinitions());
                applyPostProcessor(beansObject.get(name),name);
            }
            return beansObject.get(name);
        }
        createBean(name,bdReader.getBeanDefinitions());
        applyPostProcessor(beansObject.get(name),name);
        return beansObject.get(name);
    }

    /**
     * Получение бина по типу. Если не существует бинов такого класса  или таких бинов несколько
     * получаем исключение
     * @param type
     * @param <T>
     * @return
     */
    @Override
    public <T> T getBean(Class<T> type) throws Exception {
        int count=0;
        String name="";
        for (Map.Entry<String, BeanDefinition> entry :bdReader.getBeanDefinitions().entrySet()) {
                if (type.isAssignableFrom(entry.getValue().getBeanClass())) {
                    count++;
                    if (count==1)
                        name = entry.getKey();
                    else {name = ""; break;}
                }
        }
        if (!name.equals("")) {
            return (T) getBean(name);
        }
        else {
            throw new NullPointerException("Class not found");
            //return null;
        }
    }
}
