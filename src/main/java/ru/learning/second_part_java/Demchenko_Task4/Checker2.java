package ru.learning.second_part_java.Demchenko_Task4;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Loggable
@Order(2)
public class Checker2 implements ConveyerDataChecker{

    //проверка 2: проверяет что тип приложения соответствует одному из: “web”, “mobile”.
    //Если там записано что-либо иное, то оно преобразуется к виду “other:”+значение
    @Override
    public String check(String str) {

        String result;

        result=str;

        if(str.equals("mobile")==false && str.equals("web")==false)
            result="other:"+result;

        return result;
    }
}
