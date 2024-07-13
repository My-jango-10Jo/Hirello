package com.sparta.hirello.primary.board.repository;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> findByUser(User user, Pageable pageable);

}
