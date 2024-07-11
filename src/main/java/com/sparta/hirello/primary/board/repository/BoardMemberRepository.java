package com.sparta.hirello.primary.board.repository;

import com.sparta.hirello.primary.board.entity.BoardMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long>,
        BoardMemberRepositoryCustom {

}
