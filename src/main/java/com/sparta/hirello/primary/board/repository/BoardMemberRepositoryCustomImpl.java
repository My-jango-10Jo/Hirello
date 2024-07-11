package com.sparta.hirello.primary.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.hirello.primary.board.entity.BoardMember;
import com.sparta.hirello.primary.board.entity.QBoard;
import com.sparta.hirello.primary.board.entity.QBoardMember;
import com.sparta.hirello.primary.user.entity.QUser;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardMemberRepositoryCustomImpl implements BoardMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardMember> findByUserId(Long userId) {
        QBoardMember qBoardMember = QBoardMember.boardMember;
        QUser qUser = QUser.user;

        return queryFactory.selectFrom(qBoardMember).join(qBoardMember.user, qUser)
                .where(qBoardMember.user.id.eq(userId)).fetch();
    }

    @Override
    public Optional<BoardMember> findByUserIdAndBoardId(Long userId, Long boardId) {
        QBoardMember qBoardMember = QBoardMember.boardMember;
        QUser qUser = QUser.user;
        QBoard qBoard = QBoard.board;

        return Optional.ofNullable(queryFactory.selectFrom(qBoardMember)
                .join(qBoardMember.user, qUser)
                .join(qBoardMember.board, qBoard)
                .where(qBoardMember.user.id.eq(userId))
                .where(qBoardMember.board.boardId.eq(boardId)).fetchOne());
    }
}
