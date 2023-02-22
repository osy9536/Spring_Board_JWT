package com.board.spring_board_jwt.repository;

import com.board.spring_board_jwt.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes,Long> {


    Likes findByBoardIdAndUserId(Long BoardId, Long userId);

    Likes findByCommentIdAndUserId(Long commentId, Long id);
}
