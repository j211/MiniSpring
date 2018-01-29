package beansFactories;

import bd.BeanDefinition;
import config.AnnotatedBeanDefinitionReader;
import org.apache.log4j.Logger;
import postProcessor.BeanPostProcessor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *Создает на основе @see BeanDefinition бины и кладет в контейнер
 */
public class AnnotationBeanFactory implements BeanFactory{
    private static Logger log = Logger.getLogger(AnnotationBeanFactory.class.getName());
    private Map<String, Object> beansObject = new HashMap<>();
    private Map<String, Object> beansPostProcessor = new HashMap<>();

    private AnnotatedBeanDefinitionReader bdReader;

    public Map<String, Object> getBeansObject() {
        return beansObject;
    }

    public AnnotationBeanFactory(AnnotatedBeanDefinitionReader bdReader){
        this.bdReader = bdReader;
        beansPostProcessor = createBeans(bdReader.getBeanDefinitionsPostProcessor());
        beansObject = createBeans(bdReader.getBeanDefinitions());
        applyPostProcessors();
    }
    /**
     * Создает бин из описания @see BeanDefinition, вызвая фабричный метод
     * @param beanName - имя бина
     * @param listBeans - контейнер BeanDefinition
     */
    private Object createBean(String beanName,Map<String, BeanDefinition> listBeans) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException{
          if (listBeans.containsKey(beanName)) {
              Class className = listBeans.get(beanName).getContextBeanClass();
              Method method = className.getDeclaredMethod(listBeans.get(beanName).getFactoryMethodName());
              Object bean = method.invoke(className.newInstance(), null);
              return bean;
          }
          else {
              throw new IllegalArgumentException("Bean with the name \"" + beanName + "\" doesn't exist");
          }
    }
    /**
     * Создает бины
     * @param listBeans - контейнер BeanDefinition
     * @return - контейнер бинов
     */
    private Map<String, Object> createBeans(Map<String, BeanDefinition> listBeans){
        Map<String, Object> beans = new HashMap<>();
        for (Map.Entry<String, BeanDefinition> entry : listBeans.entrySet()) {
            try{
                beans.put(entry.getKey(),createBean(entry.getKey(),listBeans));
            }
            catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException ex){
                log.error("Beans creation exception",ex);
            }
        }
        return beans;
    }
    /**
     * Применяем постпроцессоры к переданному бину
     * @param bean - бин
     * @param beanName - имя бина
     */
    private void applyPostProcessor(Object bean, String beanName) {
        for (Map.Entry<String, Object> entryPostProcessor : beansPostProcessor.entrySet()) {
                BeanPostProcessor postProcessor = (BeanPostProcessor) entryPostProcessor.getValue();
                beansObject.put(beanName,postProcessor.postProcessBeforeInitialization(bean, beanName,this));
            }
        }
    /**
     * Настраиваем все бины с помощью постпроцессоров
     */
    private void applyPostProcessors(){
        for (Map.Entry<String, Object> entryPostProcessor : beansPostProcessor.entrySet()) {
                BeanPostProcessor postProcessor = (BeanPostProcessor) entryPostProcessor.getValue();
                for (Map.Entry<String, Object> entryBean : beansObject.entrySet()) {
                    beansObject.put(entryBean.getKey(), postProcessor.postProcessBeforeInitialization(entryBean.getValue(), entryBean.getKey(), this));
                }
        }
    }
    /**
    *Получение бина из контекста по имени
    *@param name - имя бина
    */
    @Override
    public Object getBean(String name) {
        if (beansObject.containsKey(name)) {
            if (bdReader.getBeanDefinitions().get(name).getScope().equals("prototype")) {
                try {
                    beansObject.put(name, createBean(name, bdReader.getBeanDefinitions()));
                } catch(InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException ex){
                    log.error("Bean " + name +" creation exception",ex);
                }
                applyPostProcessor(beansObject.get(name),name);
            }
            return beansObject.get(name);
        }
        try {
            createBean(name, bdReader.getBeanDefinitions());
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e){
            log.error("Bean " + name +" creation exception");
        }
        applyPostProcessor(beansObject.get(name),name);
        return beansObject.get(name);
    }

    /**
     * Получение бина по типу. Если не существует бинов такого класса или таких бинов несколько
     * получаем исключение
     * @param type - тип бина
     */
    @Override
    public <T> T getBean(Class<T> type){
        int count=0;
        String name="";
        for (Map.Entry<String, BeanDefinition> entry :bdReader.getBeanDefinitions().entrySet()) {
                if (type.isAssignableFrom(entry.getValue().getBeanClass())) {
                    count++;
                    if (count==1) {
                        name = entry.getKey();
                    }
                    else {
                        name = "";
                        break;
                    }
                }
        }
        if (!name.equals("")) {
            return (T) getBean(name);
        }
        else {
            throw new NullPointerException("Class " + type + " not found");
        }
    }
}
