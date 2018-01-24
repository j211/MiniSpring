package context;

import annotations.Bean;
import annotations.Configuration;
import annotations.Scope;
import context.Circle;
import postProcessor.AutowiredAnnotationBeanPostProcessor;
import postProcessor.BeanPostProcessor;

@Configuration
public class ContextApp {
    @Bean
    public Circle circle() { return new Circle(testBean()); }

    @Bean
    public Rectangle rectangle() {
        return new Rectangle();
    }

    @Scope(scopeName = "prototype")
    @Bean
    public Square square() {
        return new Square();
    }

    @Bean
    public TestBean testBean(){return new TestBean();}

    @Bean
    public TestBean testBean1(){return new TestBean();}
}
