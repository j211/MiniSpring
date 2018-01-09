package config;


import bd.BeanDefinition;
import bd.BeanDefinitionImpl;
import annotations.Bean;
import annotations.Configuration;
import annotations.Scope;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
//Создает описание бинов и сохранеяет в HashMap
public class AnnotatedBeanDefinitionReader {
    private  Map<String, BeanDefinition> beanDefinitions = new HashMap<>();

    public  Map<String, BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

    public void doBeanDefinition(String packageName) throws IOException, ClassNotFoundException {
        Class[] list = getClasses(packageName);
        for (Class l:list){
            if (l.isAnnotationPresent(Configuration.class))
            for (Method method : l.getMethods()) {
                if (method.isAnnotationPresent(Bean.class)) {
                    BeanDefinition beanDefinition = new BeanDefinitionImpl();
                    //beanDefinition.setFactoryMethodData(method);
                    if (method.isAnnotationPresent(Scope.class)) {
                        beanDefinition.setScope(method.getAnnotation(Scope.class).scopeName());
                    }
                    beanDefinition.setBeanClassName(method.getReturnType().getName());
                    beanDefinitions.put(method.getName(), beanDefinition);
                }
            }
        }

    }
    public static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
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
    static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
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
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

}
