package com.board.spring_board_jwt.service;

import com.board.spring_board_jwt.dto.ResponseMsgDto;
import com.board.spring_board_jwt.entity.Board;
import com.board.spring_board_jwt.entity.Comment;
import com.board.spring_board_jwt.entity.Likes;
import com.board.spring_board_jwt.entity.User;
import com.board.spring_board_jwt.repository.BoardRepository;
import com.board.spring_board_jwt.repository.CommentRepository;
import com.board.spring_board_jwt.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public ResponseMsgDto boardLike(User user, Long boardId) {
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
            return ResponseMsgDto.builder()
                    .msg("좋아요 성공!")
                    .statusCode(HttpStatus.OK.value())
                    .build();
        } else {
            likesRepository.delete(likesBoardUser);
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
        Likes likesCommentUser = likesRepository.findByCommentIdAndUserId(commentId, user.getId());
        if (likesCommentUser == null) {
            Likes likes = Likes.builder()
                    .comment(comment)
                    .user(user)
                    .build();
            likesRepository.save(likes);
            return ResponseMsgDto.builder()
                    .msg("좋아요 성공!")
                    .statusCode(HttpStatus.OK.value())
                    .build();
        } else {
            likesRepository.delete(likesCommentUser);
            return ResponseMsgDto.builder()
                    .msg("좋아요 취소 성공!")
                    .statusCode(HttpStatus.OK.value())
                    .build();
        }
    }
}
