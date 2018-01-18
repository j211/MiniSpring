package bd;

public class BeanDefinitionImpl implements BeanDefinition{
    private volatile Object beanClass;
    private volatile Object contextBeanClass;
    private String contextBeanClassName;
    private String scope= "singleton";
    private String factoryBeanName;
    private String factoryMethodName;

    public String getBeanClassName() {
        Object beanClassObject = this.beanClass;
        return beanClassObject instanceof Class ? ((Class)beanClassObject).getName() : (String)beanClassObject;
    }

    public void setBeanClass( Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() throws IllegalStateException {
        Object beanClassObject = this.beanClass;
        if (beanClassObject == null) {
            throw new IllegalStateException("No bean class specified on bean definition");
        } else if (!(beanClassObject instanceof Class)) {
            throw new IllegalStateException("Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
        } else {
            return (Class)beanClassObject;
        }
    }

    public void setContextBeanClassName( String contextBeanClassName) {
        this.contextBeanClassName = contextBeanClassName;
    }

    public String getContextBeanClassName() {
        Object beanClassObject = this.contextBeanClass;
        return beanClassObject instanceof Class ? ((Class)beanClassObject).getName() : (String)beanClassObject;
    }

    public void setContextBeanClass( Class<?> contextBeanClass) {
        this.contextBeanClass = contextBeanClass;
    }

    public Class<?> getContextBeanClass() throws IllegalStateException {
        Object beanClassObject = this.contextBeanClass;
        if (beanClassObject == null) {
            throw new IllegalStateException("No bean class specified on bean definition");
        } else if (!(beanClassObject instanceof Class)) {
            throw new IllegalStateException("Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
        } else {
            return (Class)beanClassObject;
        }
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return this.scope;
    }

    public void setFactoryBeanName( String factoryBeanName) {
    this.factoryBeanName = factoryBeanName;
}

    public String getFactoryBeanName() {
        return this.factoryBeanName;
    }

    public void setFactoryMethodName( String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    public String getFactoryMethodName() {
        return this.factoryMethodName;
    }
}
