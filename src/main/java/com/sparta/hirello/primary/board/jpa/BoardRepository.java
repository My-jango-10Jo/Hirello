package com.sparta.hirello.primary.board.jpa;

import com.sparta.hirello.primary.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, String>, BoardRepositoryCustom {
    Optional<Board> findByBoardId(Long boardId);
}
