package ru.learning.second_part_java.Demchenko_Task4;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Calendar;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Value("${spring.application.pathinvbeans}")
    private String pathinvbeans;

    @Override
    //это происходит до инит-метода бина
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        return bean;
    }

    @Override
    //это происходит после инит-метода бина
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //если нет аннотации Loggable
        if (!bean.getClass().isAnnotationPresent(Loggable.class)) return bean;
        //если есть аннотация Loggable
        //создаем экземпляр "улучшателя" - построителя новых классов(!)
        Enhancer enhancer = new Enhancer();
        //говорим, что порождаемые классы будут потомками класса нашего текущего бина
        enhancer.setSuperclass(bean.getClass());
        //добавляем обработчик, который будет перехватывать вызовы методов
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            //собственно, логика перехвата вызова метода
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                FileWriter fw;
                Parameter[] param1;
                Object retValue;

                retValue=method.invoke(bean, args);

                try {
                    fw = new FileWriter(pathinvbeans,true);
                    param1=method.getParameters();

                    fw.write("Log: bean: "+beanName+", method: "+method.getName()+", param: \""+(String)args[0]+"\", return value: \""+(String)retValue+"\", datetime: "+Calendar.getInstance().getTime()+"\n"); // запись в файл факта вызова БИНа
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                return retValue;
            }
        });

        //В задаче у нужных бинов только умолчательный конструктор, так что все ок
        Constructor cons= bean.getClass().getConstructors()[0];
        //новый массив формальных элементов по количеству аргументов конструктора
        Object [] arr=new Object[cons.getParameterCount()];
        //возвращаем экземпляр "улучшенного" бина
        return enhancer.create(cons.getParameterTypes(),arr);
    }
}

