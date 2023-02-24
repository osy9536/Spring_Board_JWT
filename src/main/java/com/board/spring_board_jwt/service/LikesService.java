package com.board.spring_board_jwt.service;

import com.board.spring_board_jwt.dto.ResponseMsgDto;
import com.board.spring_board_jwt.entity.*;
import com.board.spring_board_jwt.repository.BoardLikesRepository;
import com.board.spring_board_jwt.repository.BoardRepository;
import com.board.spring_board_jwt.repository.CommentLikeRepository;
import com.board.spring_board_jwt.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final BoardLikesRepository boardLikesRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public ResponseMsgDto boardLike(User user, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        BoardLikes boardLikesBoardUser = boardLikesRepository.findByBoardIdAndUserId(boardId,user.getId());
        if (boardLikesBoardUser == null) {
            BoardLikes boardLikes = BoardLikes.builder()
                    .board(board)
                    .user(user)
                    .build();
            boardLikesRepository.save(boardLikes);
            return ResponseMsgDto.builder()
                    .msg("좋아요 성공!")
                    .statusCode(HttpStatus.OK.value())
                    .build();
        } else {
            boardLikesRepository.delete(boardLikesBoardUser);
            return ResponseMsgDto.builder()
                    .msg("좋아요 취소 성공!")
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }
    }
    public ResponseMsgDto commentLike(User user, Long commentId ) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        CommentLikes commentLikes = commentLikeRepository.findByCommentIdAndUserId(commentId, user.getId());
        if (commentLikes == null) {
            CommentLikes commentLikes1 = CommentLikes.builder()
                    .comment(comment)
                    .user(user)
                    .build();
            commentLikeRepository.save(commentLikes1);
            return ResponseMsgDto.builder()
                    .msg("좋아요 성공!")
                    .statusCode(HttpStatus.OK.value())
                    .build();
        } else {
            commentLikeRepository.delete(commentLikes);
            return ResponseMsgDto.builder()
                    .msg("좋아요 취소 성공!")
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }
    }
}
