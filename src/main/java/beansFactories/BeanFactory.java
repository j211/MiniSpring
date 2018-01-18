package beansFactories;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public interface BeanFactory {

        Object getBean(String name) throws Exception;

        <T> T getBean(Class<T> type) throws Exception;
    }
