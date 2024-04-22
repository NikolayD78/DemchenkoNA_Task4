package ru.learning.second_part_java.Demchenko_Task4;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

interface UsersRepo extends JpaRepository<Users, Integer> {
        //List<Department> findAllByName(String name);

    @Query(value = "SELECT id,fio,username FROM users u WHERE username = :username", nativeQuery = true)
    Users selectUserFromUsers(@Param("username") String username);

    }


