package com.sparta.hirello.primary.board.repository;

import com.sparta.hirello.primary.board.entity.BoardMember;
import java.util.List;
import java.util.Optional;

public interface BoardMemberRepositoryCustom {

    List<BoardMember> findByUserId(Long userId);

    Optional<BoardMember> findByUserIdAndBoardId(Long userId, Long boardId);
}
