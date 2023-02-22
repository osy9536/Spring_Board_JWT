package com.board.spring_board_jwt.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class UserRequestDto {

    @Pattern(regexp ="^([a-z[0-9]]){4,10}$", message = "아이디는 4자 이상의 영소문자, 숫자만 가능합니다.")
    private String username;
    @Pattern(regexp ="^([a-zA-Z[0-9]]){8,15}$", message = "비밀번호는 8자 이상의 영대소문자, 숫자만 가능합니다.")
    private String password;

    private boolean admin = false;
    private String adminToken ="";
}
