package com.board.spring_board_jwt.controller;

import com.board.spring_board_jwt.dto.ResponseMsgDto;
import com.board.spring_board_jwt.security.UserDetailsImpl;
import com.board.spring_board_jwt.service.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikesController {
    private final LikesService likesService;
    @PostMapping("/post/like/{boardId}")
    public ResponseMsgDto boardLike(@PathVariable Long boardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likesService.boardLike(userDetails.getUser(), boardId);
    }

    @PostMapping("/comment/like/{commentId}")
    public ResponseMsgDto commentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return likesService.commentLike(userDetails.getUser(), commentId);
    }
}
