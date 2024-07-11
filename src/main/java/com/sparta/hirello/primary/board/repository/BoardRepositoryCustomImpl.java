package com.sparta.hirello.primary.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.entity.QBoard;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Board> findById(Long boardId) {
        QBoard qBoard = QBoard.board;

        return Optional.ofNullable(queryFactory
                .selectFrom(qBoard)
                .where(qBoard.boardId.eq(boardId))
                .fetchOne());
    }
}
