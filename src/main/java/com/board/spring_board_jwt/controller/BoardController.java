package com.board.spring_board_jwt.controller;

import com.board.spring_board_jwt.dto.BoardCommentResponseDto;
import com.board.spring_board_jwt.dto.BoardRequestDto;
import com.board.spring_board_jwt.security.UserDetailsImpl;
import com.board.spring_board_jwt.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/post")
    public ResponseEntity<Object> createBoard(@RequestBody BoardRequestDto boardRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.createBoard(userDetails.getUser(), boardRequestDto);
    }

    @GetMapping("/posts")
    public List<BoardCommentResponseDto> getBoards() {
        return boardService.getBoards();
    }

    @GetMapping("/post/{id}")
    public BoardCommentResponseDto getIdBoard(@PathVariable Long id) {
        return boardService.getIdBoard(id);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<Object> updateBoard(@PathVariable Long id,@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody BoardRequestDto boardRequestDto) {
        return boardService.updateBoard(userDetails.getUser(), id, boardRequestDto);
    }

    @DeleteMapping("post/{id}")
    public ResponseEntity<Object> deleteBoard(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deleteBoard(userDetails.getUser(), id);
    }

}
