package context;

import beansFactories.AnnotationBeanFactory;
import config.AnnotatedBeanDefinitionReader;
import postProcessor.AutowiredAnnotationBeanPostProcessor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.Exception;

public class Example {
    public static void main(String[] args) throws Exception {
        AnnotatedBeanDefinitionReader context = new AnnotatedBeanDefinitionReader();
        context.doBeanDefinition("context");
        System.out.println("BeanDefinitions");
        context.getBeanDefinitions().forEach((k, v) -> System.out.println(k+" "+ v));
        context.getBeanDefinitionsPostProcessor().forEach((k, v) -> System.out.println(k+" "+ v));
        AnnotationBeanFactory beanFactory = new AnnotationBeanFactory(context);
        System.out.println("Beans");
        beanFactory.getBeansObject().forEach((k, v) -> System.out.println(k+" "+ v));
        /*
        Circle c = (Circle)beanFactory.getBeansObject().get("circle");
        System.out.println(c.getBean());*/
        Rectangle r = (Rectangle)beanFactory.getBeansObject().get("rectangle");
        System.out.println("testBean = "+r.getTestBean());
    }
}
