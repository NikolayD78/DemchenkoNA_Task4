package ru.learning.second_part_java.Demchenko_Task4;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.HashSet;
import java.util.Set;


// Поля
// id integer
// username varchar
// fio varchar

@Entity
@Table(name = "Users")
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    public Users(String username, String fio){
        this.username = username;
        this.fio = fio;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    Integer id;

    @Column(name = "username")
    @Getter
    @Setter
    String username;

    @Column(name = "fio")
    @Getter
    @Setter
    String fio;

    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter //!!!!
    @Setter //!!!!
    Set<Logins> logins;

}
