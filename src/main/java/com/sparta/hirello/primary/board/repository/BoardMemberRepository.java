package com.sparta.hirello.primary.board.repository;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.entity.BoardMember;
import com.sparta.hirello.primary.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {

     Optional<BoardMember> findByBoardAndUser(Board board, User user);

     boolean existsByBoardAndUser(Board board, User invitedUser);

}
