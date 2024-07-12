package com.sparta.hirello.primary.board.repository;

import static com.sparta.hirello.primary.board.entity.QBoard.board;
import static com.sparta.hirello.primary.board.entity.QBoardMember.boardMember;
import static com.sparta.hirello.primary.user.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.hirello.primary.board.entity.BoardMember;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoardMemberRepositoryCustomImpl implements BoardMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardMember> findByUserId(Long userId) {

        return queryFactory.selectFrom(boardMember).join(boardMember.user, user)
                .where(boardMember.user.id.eq(userId)).fetch();
    }


    @Override
    public Optional<BoardMember> findByUserIdAndBoardId(Long userId, Long boardId) {

        return Optional.ofNullable(queryFactory.selectFrom(boardMember)
                .join(boardMember.user, user)
                .join(boardMember.board, board)
                .where(boardMember.user.id.eq(userId))
                .where(boardMember.board.boardId.eq(boardId)).fetchOne());
    }
}
