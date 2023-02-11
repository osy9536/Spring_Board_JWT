package com.board.spring_board_jwt.service;

import com.board.spring_board_jwt.dto.BoardRequestDto;
import com.board.spring_board_jwt.dto.BoardResponseDto;
import com.board.spring_board_jwt.dto.ResponseMsgDto;
import com.board.spring_board_jwt.entity.Board;
import com.board.spring_board_jwt.entity.User;
import com.board.spring_board_jwt.jwt.JwtUtil;
import com.board.spring_board_jwt.repository.BoardRepository;
import com.board.spring_board_jwt.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final JwtUtil jwtUtil;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

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
                    .title(boardRequestDto.getTitle())
                    .content(boardRequestDto.getContent())
                    .user(user)
                    .build();
                    // 요청받은 DTO 로 DB에 저장할 객체 만들기
            boardRepository.save(b);

            return BoardResponseDto.builder()
                    .title(b.title)
                    .content(b.content)
                    .createdAt(b.getCreatedAt())
                    .id(b.getId())
                    .modifiedAt(b.getModifiedAt())
                    .username(b.getUser().getUsername())
                    .build();
        } else {
            return null;
        }
    }

    @Transactional
    public List<BoardResponseDto> getBoards() {
        List<Board> boards = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardResponseDto> boardResponseDto = new ArrayList<>();
        for (Board b : boards) {
            boardResponseDto.add(BoardResponseDto.builder()
                    .title(b.title)
                    .content(b.content)
                    .createdAt(b.getCreatedAt())
                    .id(b.getId())
                    .modifiedAt(b.getModifiedAt())
                    .username(b.getUser().getUsername())
                    .build());
        }
        return boardResponseDto;
    }

    @Transactional
    public BoardResponseDto getIdBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        return BoardResponseDto.builder()
                .title(board.title)
                .content(board.content)
                .createdAt(board.getCreatedAt())
                .id(board.getId())
                .modifiedAt(board.getModifiedAt())
                .username(board.getUser().getUsername())
                .build();
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, HttpServletRequest request, BoardRequestDto boardRequestDto) {
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
            userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );

            board.update(boardRequestDto);
            // 요청받은 DTO 로 DB에 저장할 객체 만들기
            return BoardResponseDto.builder()
                    .title(board.title)
                    .content(board.content)
                    .createdAt(board.getCreatedAt())
                    .id(board.getId())
                    .modifiedAt(board.getModifiedAt())
                    .username(board.getUser().getUsername())
                    .build();
        } else {
            return null;
        }
    }

    @Transactional
    public ResponseMsgDto deleteBoard(Long id, HttpServletRequest request) {
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
            userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            boardRepository.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
            );

            boardRepository.deleteById(id);
            return ResponseMsgDto.builder()
                    .msg("게시글 삭제 성공")
                    .statusCode(HttpStatus.OK.value())
                    .build();
        } else {
            return null;
        }
    }
}
