package com.sparta.hirello.primary.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.hirello.primary.board.entity.BoardMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.sparta.hirello.primary.board.entity.QBoard.board;
import static com.sparta.hirello.primary.board.entity.QBoardMember.boardMember;
import static com.sparta.hirello.primary.user.entity.QUser.user;

@RequiredArgsConstructor
@Component
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
                .where(boardMember.board.id.eq(boardId)).fetchOne());
    }
}
