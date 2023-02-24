package com.board.spring_board_jwt.repository;

import com.board.spring_board_jwt.entity.Board;
import com.board.spring_board_jwt.entity.BoardLikes;
import com.board.spring_board_jwt.entity.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLikes,Long> {
    CommentLikes findByCommentIdAndUserId(Long commentId, Long id);
}
