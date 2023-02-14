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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BoardService {
    private final JwtUtil jwtUtil;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public BoardService(JwtUtil jwtUtil, BoardRepository boardRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.jwtUtil = jwtUtil;
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    private static ResponseEntity<Object> responseEntity(String msg) {
        return ResponseEntity.badRequest().body(ResponseMsgDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .msg(msg)
                .build());
    }

    @Transactional
    public ResponseEntity<Object> createBoard(BoardRequestDto boardRequestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        if (token == null) {
            return responseEntity("토큰이 유효하지 않습니다.");
        }
        if (!jwtUtil.validateToken(token)) {
            return responseEntity("토큰이 유효하지 않습니다.");
        }

        // 토큰에서 사용자 정보 가져오기
        Claims claims = jwtUtil.getUserInfoFromToken(token);

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
        return ResponseEntity.ok().body(BoardResponseDto.builder().board(b).build());
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
    public ResponseEntity<Object> updateBoard(Long id, HttpServletRequest request, BoardRequestDto boardRequestDto) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                return responseEntity("토큰이 유효하지 않습니다.");
            }
            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            if (Objects.equals(user.getId(), board.getUser().getId()) || user.getRole() == UserRoleEnum.ADMIN) {
                board.update(boardRequestDto);
                // 요청받은 DTO 로 DB에 저장할 객체 만들기
                return ResponseEntity.ok().body(BoardResponseDto.builder()
                        .board(board)
                        .build());
            } else {
                return responseEntity("작성자만 수정할 수 있습니다");

            }

        } else {
            return responseEntity("토큰이 유효하지 않습니다.");
        }
    }

    @Transactional
    public ResponseEntity<Object> deleteBoard(Long id, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                return responseEntity("토큰이 유효하지 않습니다.");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );
            if (Objects.equals(user.getId(), board.getUser().getId()) || user.getRole() == UserRoleEnum.ADMIN) {
                boardRepository.deleteById(id);
                return ResponseEntity.ok().body(ResponseMsgDto.builder()
                        .msg("게시글 삭제 성공")
                        .statusCode(HttpStatus.OK.value())
                        .build());
            } else {
                return ResponseEntity.ok().body(ResponseMsgDto.builder()
                        .msg("작성자만 삭제할 수 있습니다.")
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .build());

            }

        } else {
            return responseEntity("토큰이 유효하지 않습니다.");
        }
    }
}
