package com.board.spring_board_jwt.dto;

import com.board.spring_board_jwt.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardCommentResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private int likesNum;

    private List<CommentResponseDto> commentList;

    @Builder
    public BoardCommentResponseDto(Board board,List<CommentResponseDto> commentList) {
        id = board.getId();
        title = board.getTitle();
        content = board.getContent();
        username = board.getUser().getUsername();
        createdAt = board.getCreatedAt();
        modifiedAt = board.getModifiedAt();
        this.commentList = commentList;
        likesNum = board.getLikesList().size();
    }

}
