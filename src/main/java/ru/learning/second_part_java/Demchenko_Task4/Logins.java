package ru.learning.second_part_java.Demchenko_Task4;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


// поля
// id integer
// access_date timepsamp
// user_id integer
// application varchar

@Entity
@Table(name = "Logins")
@AllArgsConstructor
@NoArgsConstructor
public class Logins {


    public Logins(Timestamp access_date, Users user_id, String application){
        this.access_date = access_date;
        this.user1 = user_id;
        this.application=application;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "access_date")
    @Getter
    @Setter
    Timestamp access_date;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "user_id")
    @Getter
    @Setter
    private Users user1;

    @Column(name = "application")
    @Getter
    @Setter
    String application;

}
