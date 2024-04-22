package ru.learning.second_part_java.Demchenko_Task4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import static java.lang.Thread.sleep;

@SpringBootApplication(scanBasePackages="ru.learning.second_part_java.Demchenko_Task4")
public class DemchenkoTask4Application {

	public static void main(String[] args)
	{
		ApplicationContext ctx =SpringApplication.run(DemchenkoTask4Application.class, args);

		Conveyer conv = ctx.getBean(Conveyer.class);

		conv.perform();

	}

}
