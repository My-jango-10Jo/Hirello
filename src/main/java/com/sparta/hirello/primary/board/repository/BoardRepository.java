package com.sparta.hirello.primary.board.repository;

import com.sparta.hirello.primary.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, String>, BoardRepositoryCustom {


}
