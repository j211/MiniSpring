package context;

import beansFactories.AnnotationBeanFactory;
import config.AnnotatedBeanDefinitionReader;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import postProcessor.AutowiredAnnotationBeanPostProcessor;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.Exception;

public class Example {
    private static Logger log = Logger.getLogger(Example.class.getName());
    public static void main(String[] args) {
        log.info("Test logging");
        AnnotatedBeanDefinitionReader context = new AnnotatedBeanDefinitionReader();
        try {
            context.doBeanDefinition("context");
        }catch (IOException ex){
            log.error("Exception in doBeanDefinition ",ex);
        }
        AnnotationBeanFactory beanFactory = new AnnotationBeanFactory(context);
        System.out.println("Beans");
        beanFactory.getBeansObject().forEach((k, v) -> System.out.println("Bean name = "+k+"   Bean = "+ v));
        Circle c = (Circle)beanFactory.getBeansObject().get("circle");
        System.out.println("Field testBean in Class Circle = "+ c.getFieldBean());
        System.out.println("Field squ in Class Circle = "+ c.getSqu());
        Rectangle r = (Rectangle)beanFactory.getBeansObject().get("rectangle");
        System.out.println("Field testBean in Class Rectangle = "+r.getTestBean());
        System.out.println("Bean singleton rectangle = "+ beanFactory.getBean("rectangle"));
        System.out.println("Bean prototype square = "+ beanFactory.getBean("square"));
    }
}
