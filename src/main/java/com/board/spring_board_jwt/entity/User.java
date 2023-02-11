package com.board.spring_board_jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="users")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

//    public User(String username, String password) {
//        this.username = username;
//        this.password = password;
//    }
}
