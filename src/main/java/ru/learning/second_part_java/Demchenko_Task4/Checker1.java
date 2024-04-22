package ru.learning.second_part_java.Demchenko_Task4;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Loggable
@Order(1)
public class Checker1 implements ConveyerDataChecker{

    //проверка 1: исправляет ФИО так, чтобы каждый его компонент начинался с большой буквы
    @Override
    public String check(String str) {
        String[] fio_words = str.split(" ");
        String result;
        result="";

        for(String one_word: fio_words)
            result=result+one_word.substring(0,1).toUpperCase()+one_word.substring(1,one_word.length())+" ";

        result=result.substring(0,result.length()-1); // уберем последний пробел

        return result;

    }

}
