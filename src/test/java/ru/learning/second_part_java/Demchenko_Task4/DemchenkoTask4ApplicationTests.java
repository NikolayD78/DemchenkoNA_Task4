package ru.learning.second_part_java.Demchenko_Task4;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootTest
class DemchenkoTask4ApplicationTests {

	ApplicationContext ctx;
	Conveyer conv;
	String args;

	@Test
	void contextLoads() {

		args="";
		ctx = SpringApplication.run(DemchenkoTask4Application.class, args);
		Assertions.assertTrue(ctx!=null);

	}

	@Test
	void testChecker1() {

		args="";
		ctx = SpringApplication.run(DemchenkoTask4Application.class, args);

		Checker1 chk1=ctx.getBean(Checker1.class);
		Assertions.assertEquals(chk1.check("тестовый пользователь фио"),"Тестовый Пользователь Фио");


	}

	@Test
	void testChecker2() {

		args="";
		ctx = SpringApplication.run(DemchenkoTask4Application.class, args);

		Checker2 chk2=ctx.getBean(Checker2.class);
		Assertions.assertEquals(chk2.check("mobile"),"mobile");
		Assertions.assertEquals(chk2.check("web"),"web");
		Assertions.assertEquals(chk2.check("другое"),"other:другое");


	}

	@Test
	void testChecker3() {

		args="";
		ctx = SpringApplication.run(DemchenkoTask4Application.class, args);

		Checker3 chk3=ctx.getBean(Checker3.class);
		Assertions.assertEquals(chk3.check("2024-04-21"),"Правильно");
		Assertions.assertEquals(chk3.check("20246-04-21"),"Ошибка");



	}

	@Test
	void testDataReader() {

		args="";
		ctx = SpringApplication.run(DemchenkoTask4Application.class, args);

		ConveyerDataReader dr=ctx.getBean(ReaderFromFile.class);
		ArrayList<String> arg = dr.read();
		Assertions.assertEquals(arg.get(0),"User1;кайт том;20246-04-12;web");
		Assertions.assertEquals(arg.get(1),"User2;иванов петр петрович;20246-04-15;mobile");
		Assertions.assertEquals(arg.get(2),"User3;иванов алексей иванович;2024-04-11;web");
		Assertions.assertEquals(arg.get(3),"User4;системный администратор;2024-04-12;Linux");
		Assertions.assertEquals(arg.get(4),"User1;кайт том;2024-04-12;Oracle");
		Assertions.assertEquals(arg.get(5),"User2;иванов петр петрович;2024-04-15;mobile");
		Assertions.assertEquals(arg.get(6),"User3;иванов алексей иванович;2024-04-11;web");
		Assertions.assertEquals(arg.get(7),"User4;системный администратор;2024-04-12;web");
		Assertions.assertEquals(arg.get(8),"User1;кайт том;2024-04-12;Oralce");
		Assertions.assertEquals(arg.get(9),"User2;иванов петр петрович;2024-04-15;Postgres");
		Assertions.assertEquals(arg.get(10),"User3;иванов алексей иванович;2024-04-11;Windows");
		Assertions.assertEquals(arg.get(11),"User4;системный администратор;2024-04-12;Linux");

	}

	@Test
	void testDataWriter() {

		args = "";
		ctx = SpringApplication.run(DemchenkoTask4Application.class, args);
		File test_file;
		String result = "*Не определено*";

		ConveyerDataWriter dw = ctx.getBean(WriterToFile.class);
		dw.setPathOutput("C:\\temp\\test_file.txt");
		dw.write("Тестовая строка1.");

		try {
			test_file = new File("C:\\temp\\test_file.txt");
			Scanner sc = new Scanner(test_file);
			if (sc.hasNextLine()) {
				result = sc.nextLine();
			}
			sc.close();
			test_file.delete();
			Assertions.assertEquals(result, "Тестовая строка1.");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

	}

	// Тестовую базу не создаем, работаем с той же самой, так как установлено
	// application.properties spring.jpa.hibernate.ddl-auto=create
	@Test
	void testDBTransaction() {

		args = "";
		ctx = SpringApplication.run(DemchenkoTask4Application.class, args);

		int counter;
		int userId;
		UsersRepo ur=ctx.getBean(UsersRepo.class);
		LoginsRepo lr=ctx.getBean(LoginsRepo.class);

		Users u1=new Users("Тестовый логин","Тестовый ФИО");
		Logins l1=new Logins(Timestamp.valueOf("2024-04-22 00:00:00"),u1,"mobile");

		ur.save(u1);
		lr.save(l1);

		List<Users> listUsers = ur.findAll(Sort.by(Sort.Order.asc("username")));
		counter=0;
		userId=0;
		for (Users uCurrent :listUsers)
			{	userId=uCurrent.id; // запоминаем для тестирования что запись Logins создалась правильно

				Assertions.assertEquals(uCurrent.username,"Тестовый логин");
				Assertions.assertEquals(uCurrent.fio,"Тестовый ФИО");
				counter=counter+1;
			}
		Assertions.assertEquals(counter,1); // надо понять что именно одна запись создалась Users
												  // (не более, но и не менее)

		List<Logins> listLogins = lr.findAll(Sort.by(Sort.Order.asc("user1")));
		counter=0;
		for (Logins lCurrent :listLogins)
		{	Assertions.assertEquals(lCurrent.access_date,Timestamp.valueOf("2024-04-22 00:00:00"));
			Assertions.assertEquals(lCurrent.application,"mobile");
			Assertions.assertEquals(lCurrent.getUser1().id,userId);
			counter=counter+1;
		}
		Assertions.assertEquals(counter,1);// надо понять что именно одна запись создалась Logins
												 // (не более, но и не менее)

	}

}
