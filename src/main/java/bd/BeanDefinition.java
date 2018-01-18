package bd;

public interface BeanDefinition {
    String getBeanClassName();

    void setBeanClass( Class<?> beanClass);

    Class<?> getBeanClass() throws IllegalStateException;

    void setContextBeanClassName( String var1);

    String getContextBeanClassName();

    void setContextBeanClass( Class<?> beanClass);

    Class<?> getContextBeanClass() throws IllegalStateException;

    void setScope( String var1);

    String getScope();

    void setFactoryBeanName( String var1);

    String getFactoryBeanName();

    void setFactoryMethodName( String var1);

    String getFactoryMethodName();

}
