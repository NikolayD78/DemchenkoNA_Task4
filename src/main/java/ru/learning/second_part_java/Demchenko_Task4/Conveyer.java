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

    @Autowired
    public List<ConveyerDataChecker> checks; // проверки

    public void perform() {
        ArrayList<String> arg = dr.read(); // вычитать все строки из всех файлов в каталоге
                                           // файлы (для единообразия) должны называться input*.*

        for (String a : arg) // пробежимся по всем строкам считанным из всех файлов в каталоге

        {
            // запустим все проверки, сколько бы их ни было
            for (ConveyerDataChecker chk : checks) {
                a = chk.check(a);
            }
            dw.write(a);

        }


    }


}

