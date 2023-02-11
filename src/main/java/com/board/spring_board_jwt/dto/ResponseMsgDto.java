package com.board.spring_board_jwt.dto;

import lombok.Getter;

@Getter
public class ResponseMsgDto {
    private String msg;
    private int statusCode;

    public ResponseMsgDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }
}
