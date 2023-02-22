package com.board.spring_board_jwt.repository;

import com.board.spring_board_jwt.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByOrderByCreatedAtDesc();
}
