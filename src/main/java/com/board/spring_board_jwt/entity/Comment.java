package com.board.spring_board_jwt.entity;

import com.board.spring_board_jwt.dto.CommentRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;
    @Column(nullable = false)
    private String username;

    @ManyToOne
    @JoinColumn(name = "USERS_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID", nullable = false)
    private Board board;

    public void setBoard(Board board) {
        if (this.board != null) {
            this.board.getCommentList().remove(this);
        }
        this.board = board;
        board.getCommentList().add(this);
    }
    @Builder
    public Comment(CommentRequestDto requestDto,User user, Board board) {
        this.comment = requestDto.getComment();
        this.username = user.getUsername();
        this.board = board;
        this.user = user;
    }

    public void update(CommentRequestDto requestDto){
        this.comment = requestDto.getComment();
    }
}
