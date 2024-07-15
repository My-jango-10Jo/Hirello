package com.sparta.hirello.primary.board.repository;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.entity.BoardMember;
import com.sparta.hirello.primary.user.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("select bm from BoardMember bm where bm.id in :id ")
    Optional<BoardMember> findByBoardAndUserWithPessimisticLock(Board board, User user);

    boolean existsByBoardAndUser(Board board, User invitedUser);

}