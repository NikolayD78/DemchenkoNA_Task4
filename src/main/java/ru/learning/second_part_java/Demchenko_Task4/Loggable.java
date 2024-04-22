package ru.learning.second_part_java.Demchenko_Task4;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Loggable {
    String value() default "C:/temp/logs_component.log";
}
