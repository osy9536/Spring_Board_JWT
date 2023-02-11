package com.board.spring_board_jwt.entity;

import com.board.spring_board_jwt.dto.BoardRequestDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(nullable = false)
    public String title;

    @Column(nullable = false)
    public String content;

    @ManyToOne
    @JoinColumn(name = "USERS_ID", nullable = false)
    private User user;
    @Builder
    public Board(BoardRequestDto boardRequestDto, User user) {
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.user = user;
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.content = boardRequestDto.getContent();
        this.title = boardRequestDto.getTitle();
    }
}
