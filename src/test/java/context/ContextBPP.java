package context;

import annotations.Bean;
import annotations.Configuration;
import postProcessor.AutowiredAnnotationBeanPostProcessor;
import postProcessor.BeanPostProcessor;

@Configuration
public class ContextBPP {
    @Bean
    public BeanPostProcessor autowiredAnnotationBeanPostProcessor(){return new AutowiredAnnotationBeanPostProcessor();}
}
