package ru.learning.second_part_java.Demchenko_Task4;

import org.springframework.data.jpa.repository.JpaRepository;


interface LoginsRepo extends JpaRepository<Logins, Integer> {
        //List<Department> findAllByName(String name);

    }


