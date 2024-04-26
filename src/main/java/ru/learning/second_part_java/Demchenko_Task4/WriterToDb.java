package ru.learning.second_part_java.Demchenko_Task4;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class WriterToDb implements ConveyerDataWriter {

    // бины для записи в БД
    @Setter
    @Getter
    @Autowired
    LoginsRepo lr;
    @Setter
    @Getter
    @Autowired
    UsersRepo ur;

    @Override
    public void write(String str) {

        int needSaveUser;

        // если была ошибка в дате - записывать в базу не надо
        if (str.equals("Ошибка"))
            return;

        String[] words = str.split(";"); // делим на элементы

        Users u1 = new Users("Не определено", "Не определено");
        try {
            u1 = ur.selectUserFromUsers(words[0]);
        } catch (InvalidDataAccessResourceUsageException | NullPointerException e) {
        }

        needSaveUser = 0;
        if (u1 != null)
            System.out.println("Уже есть в базе: " + u1.username);
        if (u1 == null) {
            u1 = new Users(words[0], words[1]);
            needSaveUser = 1;
        }

        if (needSaveUser == 1)
            ur.save(u1); //запись пользователя в базу

        lr.save(new Logins(Timestamp.valueOf(words[2] + " 00:00:00"), u1, words[3])); // запись в базу

    }

}