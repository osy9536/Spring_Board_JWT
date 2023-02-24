package com.board.spring_board_jwt.repository;

import com.board.spring_board_jwt.entity.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikesRepository extends JpaRepository<BoardLikes,Long> {
    BoardLikes findByBoardIdAndUserId(Long BoardId, Long userId);
}
