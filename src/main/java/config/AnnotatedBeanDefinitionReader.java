package config;


import bd.BeanDefinition;
import bd.BeanDefinitionImpl;
import annotations.Bean;
import annotations.Configuration;
import annotations.Scope;
import postProcessor.BeanPostProcessor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * Создает описание бинов и сохранеяет в контейнер
 */
public class AnnotatedBeanDefinitionReader {
    /**Контейнер для хранения BeanDefinition*/
    private  Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    /**Котейнер для хранения BeanPostProcessors*/
    private  Map<String, BeanDefinition> beanDefinitionsPostProcessor = new HashMap<>();

    public  Map<String, BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    public Map<String, BeanDefinition> getBeanDefinitionsPostProcessor() {
        return beanDefinitionsPostProcessor;
    }

    /**
     * Сканирование конфигурации и поиск бинов, создание BeanDefinition
     * @param packageName - имя пакета
     */
    public void doBeanDefinition(String packageName) throws IOException{
        Class[] list = getClasses(packageName);
        for (Class l:list){
            if (l.isAnnotationPresent(Configuration.class))
            for (Method method : l.getMethods()) {
                if (method.isAnnotationPresent(Bean.class)) {
                    BeanDefinition beanDefinition = new BeanDefinitionImpl();
                    beanDefinition.setFactoryMethodName(method.getName());
                    if (method.isAnnotationPresent(Scope.class)) {
                        beanDefinition.setScope(method.getAnnotation(Scope.class).scopeName());
                    }
                    beanDefinition.setBeanClass(method.getReturnType());
                    beanDefinition.setContextBeanClass(method.getDeclaringClass());
                    if (method.getReturnType().getSimpleName().equals("BeanPostProcessor"))
                        beanDefinitionsPostProcessor.put(method.getName(), beanDefinition);
                    else
                        beanDefinitions.put(method.getName(), beanDefinition);
                }
            }
        }
    }

    /**
     * Получение списка классов в указанном пакете
     * @param packageName - имя пакета
     * @return - список классов в пакете
     */
    public static Class[] getClasses(String packageName) throws IOException{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList();
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        List<Class> classes = new ArrayList();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**Поиск класса
     * @param directory - директория
     * @param packageName - имя пакета
     */
    static List<Class> findClasses(File directory, String packageName){
        List<Class> classes = new ArrayList();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                try {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                } catch(ClassNotFoundException ex){
                    System.out.println("ClassNotFoundException in findClasses");
                }
            }
        }
        return classes;
    }
}
