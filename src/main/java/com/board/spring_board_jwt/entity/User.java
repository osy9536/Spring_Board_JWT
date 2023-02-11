package com.board.spring_board_jwt.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name="users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    @Builder
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
