package com.sparta.hirello.primary.board.repository;

import com.sparta.hirello.primary.board.entity.Board;
import java.util.Optional;

public interface BoardRepositoryCustom {

    Optional<Board> findById(Long boardId);
}
