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


    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        if (token != null) {

            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("ID가 존재하지 않습니다.")
            );

            Comment comment = Comment.builder()
                    .requestDto(requestDto)
                    .board(board)
                    .user(user)
                    .build();
            commentRepository.save(comment);
            return CommentResponseDto.builder()
                    .comment(comment)
                    .build();
        }
        return null;
    }

    @Transactional
    public CommentResponseDto update(Long id,CommentRequestDto commentRequestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;


        if (token != null) {

            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );

            Comment comment = commentRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

            if (Objects.equals(comment.getUser().getId(), user.getId()) || user.getRole().equals(UserRoleEnum.ADMIN)) {

                comment.update(commentRequestDto);
                return new CommentResponseDto(comment);
            } else {
                throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
            }
        }
        return null;
    }
    @Transactional
    public ResponseMsgDto delete(Long id, HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {

            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );

            Comment comment = commentRepository.findById(id).orElseThrow(() ->
                    new IllegalArgumentException("댓글이 존재하지 않습니다."));

            if (Objects.equals(comment.getUser().getId(), user.getId()) || user.getRole().equals(UserRoleEnum.ADMIN)) {

                commentRepository.deleteById(id);
                return ResponseMsgDto.builder()
                        .msg("댓글 삭제 성공")
                        .statusCode(200)
                        .build();
            } else {
                throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
            }


        }
        return null;
    }
}
