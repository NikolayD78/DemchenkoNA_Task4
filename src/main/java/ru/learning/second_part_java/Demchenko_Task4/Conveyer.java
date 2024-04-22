package ru.learning.second_part_java.Demchenko_Task4;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
class Conveyer {


    // бины для обработки файла
    @Setter
    @Getter
    @Autowired
    ConveyerDataReader dr;
    @Setter
    @Getter
    @Autowired
    ConveyerDataWriter dw;
    // бины проверок
    @Setter
    @Getter
    @Autowired
    Checker1 chk1;
    @Setter
    @Getter
    @Autowired
    Checker2 chk2;
    @Setter
    @Getter
    @Autowired
    Checker3 chk3;
    // бины для записи в БД
    @Setter
    @Getter
    @Autowired
    LoginsRepo lr;
    @Setter
    @Getter
    @Autowired
    UsersRepo ur;

    @Value("${spring.application.pathbadrecs}")
    private String pathbadrecs;


    //@Autowired
    //public List<ConveyerDataChecker> checks;// = new ArrayList<>();

    public void perform() {
        ArrayList<String> arg = dr.read();
        int needSaveUser;
        //for (ConveyerDataChecker chk : checks) {
            //arg = chk.check(arg);
        //}

        for(String a: arg)
            {   String[] words = a.split(";");

                Users u1=new Users("Не определено","Не определено");
                try
                    {u1=ur.selectUserFromUsers(words[0]);}
                catch (InvalidDataAccessResourceUsageException | NullPointerException e){}

                needSaveUser=0;
                if (u1!=null)
                    System.out.println("Уже есть в базе: "+u1.username);
                if (u1==null)
                    {u1=new Users(words[0],chk1.check(words[1]));
                     needSaveUser=1;}

                // проверим правильно ли задана дата, если нет,то не будем ничего записывать в базу!!!
                if(chk3.check(words[2]).equals("Правильно"))
                    {
                     if(needSaveUser==1)
                         ur.save(u1); //запись пользователя в базу

                     words[3]=chk2.check(words[3]);
                     lr.save(new Logins(Timestamp.valueOf(words[2]+" 00:00:00"),u1,words[3])); // запись в базу
                     dw.write(a);// запись в файл
                    }

                if(chk3.check(words[2]).equals("Ошибка"))
                    {   FileWriter fw;

                        try {
                            fw = new FileWriter(pathbadrecs,true);
                            fw.write(a+"\n"); // запись ошибочной строки в отдельный лог
                            fw.close();
                         } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }


            }
    }

    ;
}

