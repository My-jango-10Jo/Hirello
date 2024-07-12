package com.sparta.hirello.primary.board.repository;

import com.sparta.hirello.primary.board.entity.BoardMember;
import java.util.List;
import java.util.Optional;

public interface BoardMemberRepositoryCustom {

    //유저가 어떤 보드의 맴버인지 확인 하기 위한 정보
    List<BoardMember> findByUserId(Long userId);

    //맴버의 속한 유저 정보 존재 하는지 확인
    Optional<BoardMember> findByUserIdAndBoardId(Long userId, Long boardId);
}
