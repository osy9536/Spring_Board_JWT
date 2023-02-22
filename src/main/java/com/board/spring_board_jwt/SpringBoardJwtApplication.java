package com.board.spring_board_jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableAspectJAutoProxy
@SpringBootApplication
public class SpringBoardJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoardJwtApplication.class, args);
    }

}
