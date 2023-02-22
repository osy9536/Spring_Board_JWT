package com.board.spring_board_jwt.dto;

import com.board.spring_board_jwt.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String username;
    private int likeNum;
    @Builder
    public CommentResponseDto(Comment comment) {
        id = comment.getId();
        this.comment = comment.getComment();
        createdAt = comment.getBoard().getCreatedAt();
        modifiedAt = comment.getBoard().getModifiedAt();
        username = comment.getUser().getUsername();
        likeNum = comment.getLikesList().size();
    }
}
