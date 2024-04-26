package ru.learning.second_part_java.Demchenko_Task4;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

@Component
@Loggable
@Order(3)
public class Checker3 implements ConveyerDataChecker{

    @Value("${spring.application.pathbadrecs}")
    private String pathbadrecs;

    //проверка 3: Промежуточная компонента проверки даты проверяет её наличие. Если дата не задана, то человек не вносится в базу, а сведения о имени файла и значении человека заносятся в отдельный лог
    @Override
    public String check(String str) {



        Timestamp time1;
        String[] words = str.split(";"); // делим на элементы

        try
        {
            time1= Timestamp.valueOf(words[2]+" 00:00:00");
        }catch(IllegalArgumentException e){
               FileWriter fw;

                try {
                    fw = new FileWriter(pathbadrecs,true);
                    fw.write(str+"\n"); // запись ошибочной строки в отдельный лог
                    fw.close();
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            return "Ошибка";
        }
        return str;
    }


}
