package com.board.spring_board_jwt.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
public class UserRequestDto {
    private String username;
    private String password;
    private boolean admin = false;
    private String adminToken ="";
}
