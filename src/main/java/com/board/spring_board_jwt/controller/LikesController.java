package com.board.spring_board_jwt.controller;

import com.board.spring_board_jwt.dto.BoardRequestDto;
import com.board.spring_board_jwt.security.UserDetailsImpl;
import com.board.spring_board_jwt.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;
    @PostMapping("/post/like/{boardId}")
    public ResponseEntity<Object> boardLike(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likesService.boardLike(boardId, userDetails.getUser());
    }

    @PostMapping("/comment/like/{commentId}")
    public ResponseEntity<Object> commentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likesService.commentLike(commentId, userDetails.getUser());
    }
}
