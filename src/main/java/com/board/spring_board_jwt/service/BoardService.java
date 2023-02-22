package com.board.spring_board_jwt.service;

import com.board.spring_board_jwt.dto.*;
import com.board.spring_board_jwt.entity.Board;
import com.board.spring_board_jwt.entity.Comment;
import com.board.spring_board_jwt.entity.User;
import com.board.spring_board_jwt.entity.UserRoleEnum;
import com.board.spring_board_jwt.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponseDto createBoard( User user,BoardRequestDto boardRequestDto) {
        Board b = Board.builder()
                .boardRequestDto(boardRequestDto)
                .user(user)
                .build();

        // 요청받은 DTO 로 DB에 저장할 객체 만들기
        boardRepository.save(b);
        return BoardResponseDto.builder().board(b).build();
    }

    @Transactional
    public List<BoardCommentResponseDto> getBoards() {
        List<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardCommentResponseDto> boardResponseDto = new ArrayList<>();
        for (Board b : boards) {
            b.getCommentList().sort(Comparator.comparing(Comment::getCreatedAt).reversed());
            List<CommentResponseDto> commentResponseDto1 = new ArrayList<>();
            for (Comment c : b.getCommentList()) {
                commentResponseDto1.add(new CommentResponseDto(c));
            }
            boardResponseDto.add(BoardCommentResponseDto.builder()
                    .board(b)
                    .commentList(commentResponseDto1)
                    .build());
        }
        return boardResponseDto;
    }

    @Transactional
    public BoardCommentResponseDto getIdBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        board.getCommentList().sort(Comparator.comparing(Comment::getCreatedAt).reversed());
        List<CommentResponseDto> commentResponseDto1 = new ArrayList<>();
        for (Comment c : board.getCommentList()) {
                commentResponseDto1.add(new CommentResponseDto(c));
        }
        return BoardCommentResponseDto.builder()
                .commentList(commentResponseDto1)
                .board(board)
                .build();
    }

    @Transactional
    public BoardResponseDto updateBoard(User user,Long id,  BoardRequestDto boardRequestDto) {

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

        if (Objects.equals(user.getId(), board.getUser().getId()) || user.getRole() == UserRoleEnum.ADMIN) {
            board.update(boardRequestDto);
            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            return BoardResponseDto.builder()
                    .board(board)
                    .build();
        } else {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다");
        }
    }

    @Transactional
    public ResponseMsgDto deleteBoard(User user,Long id ) {

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if (Objects.equals(user.getId(), board.getUser().getId()) || user.getRole() == UserRoleEnum.ADMIN) {
            boardRepository.deleteById(id);
            return ResponseMsgDto.builder()
                    .msg("게시글 삭제 성공")
                    .statusCode(HttpStatus.OK.value())
                    .build();
        } else {
            return ResponseMsgDto.builder()
                    .msg("작성자만 삭제할 수 있습니다.")
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
    }
}
