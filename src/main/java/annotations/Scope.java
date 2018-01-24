package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация, установливающая область видимости бина
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    /**Свойство - имя области видимости, по умолчанию singleton, можно изменить на prototype*/
    String scopeName() default "singleton";
}
