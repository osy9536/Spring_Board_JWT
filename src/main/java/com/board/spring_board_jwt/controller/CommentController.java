package com.board.spring_board_jwt.controller;

import com.board.spring_board_jwt.dto.CommentRequestDto;
import com.board.spring_board_jwt.security.UserDetailsImpl;
import com.board.spring_board_jwt.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{boardId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long boardId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return commentService.createComment(userDetails.getUser(), boardId, requestDto);
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<Object> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.update(userDetails.getUser(), id, commentRequestDto);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Object> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return commentService.delete(userDetails.getUser(), id);
    }
}
