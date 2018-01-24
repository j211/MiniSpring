package beansFactories;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Фабрика бинов
 */
public interface BeanFactory {
    /**
     * Получение бина по иени
     * @param name - имя бина
     * @return - бин
     */
    Object getBean(String name);

    /**
     * Получение бина по классу
     * @param type - класс бина
     * @param <T> - параметризованный тип
     * @return - бин
     */
    <T> T getBean(Class<T> type);
    }
