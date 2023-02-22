package com.board.spring_board_jwt.service;

import com.board.spring_board_jwt.dto.BoardRequestDto;
import com.board.spring_board_jwt.dto.ResponseMsgDto;
import com.board.spring_board_jwt.entity.*;
import com.board.spring_board_jwt.repository.BoardRepository;
import com.board.spring_board_jwt.repository.CommentRepository;
import com.board.spring_board_jwt.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public ResponseEntity<Object> boardLike(Long boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        Likes likesBoardUser = likesRepository.findByBoardIdAndUserId(boardId,user.getId());
        if (likesBoardUser == null) {
            Likes likes = Likes.builder()
                    .board(board)
                    .user(user)
                    .build();
            likesRepository.save(likes);
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 성공!");
        } else {
            likesRepository.delete(likesBoardUser);
            return ResponseEntity.status(HttpStatus.OK).body("좋아요 취소 성공!");
        }
    }

    public ResponseEntity<Object> commentLike(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        Likes likesCommentUser = likesRepository.findByCommentIdAndUserId(commentId, user.getId());
        if (likesCommentUser == null) {
            Likes likes = Likes.builder()
                    .comment(comment)
                    .user(user)
                    .build();
            likesRepository.save(likes);
            ResponseMsgDto ok = ResponseMsgDto.builder()
                    .msg("좋아요 성공!")
                    .statusCode(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(ok);
        } else {
            likesRepository.delete(likesCommentUser);
            ResponseMsgDto ok = ResponseMsgDto.builder()
                    .msg("좋아요 취소 성공!")
                    .statusCode(HttpStatus.OK.value())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
    }
}
