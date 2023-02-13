package com.board.spring_board_jwt.dto;

import com.board.spring_board_jwt.entity.Board;
import com.board.spring_board_jwt.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private String msg;
    private int statusCode;

    @Builder
    public BoardResponseDto(Board board) {
        id = board.getId();
        title = board.getTitle();
        content = board.getContent();
        username = board.getUser().getUsername();
        createdAt = board.getCreatedAt();
        modifiedAt = board.getModifiedAt();
    }

    @Builder(builderMethodName = "BoardResponseDto_Msg")
    public BoardResponseDto(ResponseMsgDto responseMsgDto) {
        msg = responseMsgDto.getMsg();
        statusCode = responseMsgDto.getStatusCode();
    }
}
