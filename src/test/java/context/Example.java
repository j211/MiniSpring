package context;

import config.AnnotatedBeanDefinitionReader;

import java.io.IOException;

public class Example {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        AnnotatedBeanDefinitionReader a = new AnnotatedBeanDefinitionReader();
        a.doBeanDefinition("context");
        a.getBeanDefinitions().forEach((k, v) -> System.out.println(k));
    }
}
