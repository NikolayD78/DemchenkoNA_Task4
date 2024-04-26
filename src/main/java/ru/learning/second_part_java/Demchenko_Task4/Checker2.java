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

        String[] words = str.split(";"); // делим на элементы
        String result;
        result="";

        result=words[3];

        if(result.equals("mobile")==false && result.equals("web")==false)
            result="other:"+result;

        return words[0]+";"+words[1]+";"+words[2]+";"+result;
    }
}
