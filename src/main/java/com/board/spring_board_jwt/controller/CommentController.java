package com.board.spring_board_jwt.controller;

import com.board.spring_board_jwt.dto.CommentRequestDto;
import com.board.spring_board_jwt.dto.CommentResponseDto;
import com.board.spring_board_jwt.dto.ResponseMsgDto;
import com.board.spring_board_jwt.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{boardId}/comment")
    public CommentResponseDto createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto requestDto, HttpServletRequest request){

        return commentService.createComment(boardId,requestDto,request);
    }

    @PutMapping("/comment/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id,@RequestBody CommentRequestDto commentRequestDto,HttpServletRequest request){
        return commentService.update(id,commentRequestDto,request);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseMsgDto deleteComment(@PathVariable Long id, HttpServletRequest request){
        return commentService.delete(id,request);
    }
}
