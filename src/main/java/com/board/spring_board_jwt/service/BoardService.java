package com.board.spring_board_jwt.service;

import com.board.spring_board_jwt.dto.*;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final JwtUtil jwtUtil;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Board b = Board.builder()
                    .boardRequestDto(boardRequestDto)
                    .user(user)
                    .build();

            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            boardRepository.save(b);
            return BoardResponseDto.builder()
                    .board(b)
                    .build();
        } else {
            return null;
        }
    }

    @Transactional
    public List<BoardCommentResponseDto> getBoards() {
        List<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardCommentResponseDto> boardResponseDto = new ArrayList<>();
        List<Comment> allCommentList = commentRepository.findAllByOrderByCreatedAtDesc();


        for (Board b : boards) {
            List<CommentResponseDto> commentResponseDto1 = new ArrayList<>();
            for (Comment c : allCommentList) {
                if (Objects.equals(b.getId(), c.getBoard().getId())) {
                    CommentResponseDto commentResponseDto = new CommentResponseDto(c);
                    commentResponseDto1.add(commentResponseDto);
                }
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
        List<Comment> allCommentList = commentRepository.findAllByOrderByCreatedAtDesc();
        List<CommentResponseDto> commentResponseDto1 = new ArrayList<>();
        for (Comment c : allCommentList) {
            if (Objects.equals(board.getId(), c.getBoard().getId())) {
                CommentResponseDto commentResponseDto = new CommentResponseDto(c);
                commentResponseDto1.add(commentResponseDto);
            }
        }
        return BoardCommentResponseDto.builder()
                .commentList(commentResponseDto1)
                .board(board)
                .build();
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, HttpServletRequest request, BoardRequestDto boardRequestDto) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        //작성자 다른 에러
        ResponseMsgDto responseMsgDto = ResponseMsgDto.builder()
                .msg("작성자만 수정할 수 있습니다.")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        //토큰 유요성 에러
        ResponseMsgDto responseMsgDto1 = ResponseMsgDto.builder()
                .msg("토큰이 유효하지 않습니다.")
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .build();
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                return BoardResponseDto.BoardResponseDto_Msg()
                        .responseMsgDto(responseMsgDto)
                        .build();
            }
            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            if (Objects.equals(user.getId(), board.getUser().getId()) || user.getRole() == UserRoleEnum.ADMIN) {
                board.update(boardRequestDto);
                // 요청받은 DTO 로 DB에 저장할 객체 만들기
                return BoardResponseDto.builder()
                        .board(board)
                        .build();
            } else {
                return BoardResponseDto.BoardResponseDto_Msg()
                        .responseMsgDto(responseMsgDto1)
                        .build();

            }

        } else {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다!");
        }
    }

    @Transactional
    public ResponseMsgDto deleteBoard(Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        User u = userRepository.findByUsername(jwtUtil.getUserInfoFromToken(token).getSubject()).orElseThrow();
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else if (u.getRole() == UserRoleEnum.ADMIN) {
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
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

        } else {
            return null;
        }
    }
}
