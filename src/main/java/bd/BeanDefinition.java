package bd;

public interface BeanDefinition {
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void setBeanClassName( String var1);

    String getBeanClassName();

    void setScope( String var1);

    String getScope();

    void setFactoryBeanName( String var1);


    String getFactoryBeanName();

    void setFactoryMethodName( String var1);

    String getFactoryMethodName();

}
