package ru.learning.second_part_java.Demchenko_Task4;

import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootTest
@Testcontainers
class DemchenkoTask4ApplicationTests {

	String args;

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:alpine3.19")
			.withDatabaseName("jpadb")
			.withUsername("postgres")
			.withPassword("12345");

	@BeforeAll
	static void beforeAll(ApplicationContext context) {
		postgres.start();

	}

	@AfterAll
	static void afterAll() {
		postgres.stop();
	}


	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {

		//System.out.println("postgres::getJdbcUrl "+postgres.getJdbcUrl());
		//System.out.println("postgres::getUsername "+postgres.getUsername());
		//System.out.println("postgres::getPassword "+postgres.getPassword());

		registry.add("spring.datasource.url", postgres::getJdbcUrl);
		registry.add("spring.datasource.name", postgres::getUsername);
		registry.add("spring.datasource.password", postgres::getPassword);
	}

	@Test
	void contextLoads(ApplicationContext context) {
		Assertions.assertNotNull(context);
	}

	@Test
	void pgContainerIsUp() {
		Assertions.assertEquals("running", postgres.getContainerInfo().getState().getStatus());
	}

	@Test
	void testChecker1(ApplicationContext context, @Autowired Checker1 chk1) {

		Assertions.assertEquals(chk1.check("User3;иванов алексей иванович;2024-04-11;web"), "User3;Иванов Алексей Иванович;2024-04-11;web");

	}

	@Test
	void testChecker2(ApplicationContext context, @Autowired Checker2 chk2) {

		Assertions.assertEquals(chk2.check("User3;иванов алексей иванович;2024-04-11;web"),"User3;иванов алексей иванович;2024-04-11;web");
		Assertions.assertEquals(chk2.check("User3;иванов алексей иванович;2024-04-11;mobile"),"User3;иванов алексей иванович;2024-04-11;mobile");
		Assertions.assertEquals(chk2.check("User3;иванов алексей иванович;2024-04-11;другое"),"User3;иванов алексей иванович;2024-04-11;other:другое");


	}

	@Test
	void testChecker3(ApplicationContext context, @Autowired Checker3 chk3) {

		Assertions.assertEquals(chk3.check("User3;иванов алексей иванович;2024-04-11;web"),"User3;иванов алексей иванович;2024-04-11;web");
		Assertions.assertEquals(chk3.check("User3;иванов алексей иванович;20246-04-11;web"),"Ошибка");

	}

	@Test
	void testCheckerAll(ApplicationContext context, @Autowired List<ConveyerDataChecker> checks) {

		String checkValue;
		checkValue="User3;иванов алексей иванович;2024-04-11;linux";

		for (ConveyerDataChecker chk : checks)
				checkValue = chk.check(checkValue);

		Assertions.assertEquals(checkValue,"User3;Иванов Алексей Иванович;2024-04-11;other:linux");

	}

	@Test
	void testDataReader(ApplicationContext context, @Autowired ConveyerDataReader dr) {


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

	// Записываем одну запись в таблицу users и одну в logins и проверяем что именно так и записалось
	@Test
	void testDBTransaction(ApplicationContext context, @Autowired UsersRepo ur,@Autowired LoginsRepo lr) {


		int counter;
		int userId;

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

		List<Logins> listLogins = lr.findAll(Sort.by(Sort.Order.asc("user1")));
		counter=0;
		for (Logins lCurrent :listLogins)
		{	Assertions.assertEquals(lCurrent.access_date,Timestamp.valueOf("2024-04-22 00:00:00"));
			Assertions.assertEquals(lCurrent.application,"mobile");
			Assertions.assertEquals(lCurrent.getUser1().id,userId);
			counter=counter+1;
		}
		Assertions.assertEquals(counter,1);// надо понять что именно одна запись создалась Logins

	}





}

