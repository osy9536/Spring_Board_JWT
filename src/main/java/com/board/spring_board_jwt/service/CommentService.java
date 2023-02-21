package com.board.spring_board_jwt.service;

import com.board.spring_board_jwt.dto.CommentRequestDto;
import com.board.spring_board_jwt.dto.CommentResponseDto;
import com.board.spring_board_jwt.dto.ResponseMsgDto;
import com.board.spring_board_jwt.entity.Board;
import com.board.spring_board_jwt.entity.Comment;
import com.board.spring_board_jwt.entity.User;
import com.board.spring_board_jwt.entity.UserRoleEnum;
import com.board.spring_board_jwt.jwt.JwtUtil;
import com.board.spring_board_jwt.repository.BoardRepository;
import com.board.spring_board_jwt.repository.CommentRepository;
import com.board.spring_board_jwt.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BoardRepository boardRepository;

    private static ResponseEntity<Object> responseEntity(String msg) {
        return ResponseEntity.badRequest().body(ResponseMsgDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .msg(msg)
                .build());
    }

    @Transactional
    public ResponseEntity<Object> createComment(Long id, CommentRequestDto requestDto, User user) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        Comment comment = Comment.builder()
                .requestDto(requestDto)
                .board(board)
                .user(user)
                .build();
        commentRepository.save(comment);
        return ResponseEntity.ok().body(CommentResponseDto.builder()
                .comment(comment)
                .build());
    }

    @Transactional
    public ResponseEntity<Object> update(Long id, CommentRequestDto commentRequestDto, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (Objects.equals(comment.getUser().getId(), user.getId()) || user.getRole().equals(UserRoleEnum.ADMIN)) {

            comment.update(commentRequestDto);
            return ResponseEntity.ok().body(new CommentResponseDto(comment));
        } else {
            return responseEntity("작성자만 수정할 수 있습니다.");
        }
    }

    @Transactional
    public ResponseEntity<Object> delete(Long id, User user) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (Objects.equals(comment.getUser().getId(), user.getId()) || user.getRole().equals(UserRoleEnum.ADMIN)) {

            commentRepository.deleteById(id);
            return ResponseEntity.ok().body(ResponseMsgDto.builder()
                    .msg("댓글 삭제 성공")
                    .statusCode(200)
                    .build());
        } else {
            return responseEntity("작성자만 삭제할 수 있습니다.");
        }
    }
}
