package com.board.spring_board_jwt.controller;

import com.board.spring_board_jwt.dto.BoardCommentResponseDto;
import com.board.spring_board_jwt.dto.BoardRequestDto;
import com.board.spring_board_jwt.dto.BoardResponseDto;
import com.board.spring_board_jwt.dto.ResponseMsgDto;
import com.board.spring_board_jwt.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/post")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto boardRequestDto, HttpServletRequest request) {
        return boardService.createBoard(boardRequestDto, request);
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
    public BoardResponseDto updateBoard(@PathVariable Long id, HttpServletRequest request, @RequestBody BoardRequestDto boardRequestDto) {
        return boardService.updateBoard(id, request, boardRequestDto);
    }

    @DeleteMapping("post/{id}")
    public ResponseMsgDto deleteBoard(@PathVariable Long id, HttpServletRequest request) {
        return boardService.deleteBoard(id, request);
    }

}
