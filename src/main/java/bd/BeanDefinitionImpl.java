package bd;
/*
*Хранит метаданные будующих бинов
 */
public class BeanDefinitionImpl implements BeanDefinition{
    private volatile Object beanClass;
    private volatile Object contextBeanClass;
    private String contextBeanClassName;
    private String scope= "singleton";
    private String factoryBeanName;
    private String factoryMethodName;

    @Override
    public String getBeanClassName() {
        Object beanClassObject = this.beanClass;
        return beanClassObject instanceof Class ? ((Class)beanClassObject).getName() : (String)beanClassObject;
    }

    @Override
    public void setBeanClass( Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
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

    @Override
    public void setContextBeanClassName( String contextBeanClassName) {
        this.contextBeanClassName = contextBeanClassName;
    }

    @Override
    public String getContextBeanClassName() {
        Object beanClassObject = this.contextBeanClass;
        return beanClassObject instanceof Class ? ((Class)beanClassObject).getName() : (String)beanClassObject;
    }

    @Override
    public void setContextBeanClass( Class<?> contextBeanClass) {
        this.contextBeanClass = contextBeanClass;
    }

    @Override
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

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public void setFactoryBeanName( String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override
    public String getFactoryBeanName() {
        return this.factoryBeanName;
    }

    @Override
    public void setFactoryMethodName( String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    @Override
    public String getFactoryMethodName() {
        return this.factoryMethodName;
    }
}
