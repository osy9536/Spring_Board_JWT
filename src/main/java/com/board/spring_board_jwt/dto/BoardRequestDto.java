package com.board.spring_board_jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardRequestDto {
    private String title;
    private String content;
}
