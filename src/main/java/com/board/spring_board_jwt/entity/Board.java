package com.board.spring_board_jwt.entity;

import com.board.spring_board_jwt.dto.BoardRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String title;

    @Column(nullable = false)
    public String content;

    @ManyToOne
    @JoinColumn(name = "USERS_ID", nullable = false)
    private User user;
    // cascade는 부모가 자식의 생명주기를 갖고있고,
    // orphanRemoval는 부모와 상관없이 자식이 고아객체가 되면 삭제됨(부모가 2개있을수도 있음)
    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL)
    // OnDelete는 Db 테이블에 접근해서 ON CASCADE DELETE를 붙혀준다
    // 하지만 JPA 자체가 DB 저장소에 구애받지 않고 사용하기위한 기술이기 때문에
    // @OnDelete는 지양해야한다.
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardLikes> boardLikes = new ArrayList<>();
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
