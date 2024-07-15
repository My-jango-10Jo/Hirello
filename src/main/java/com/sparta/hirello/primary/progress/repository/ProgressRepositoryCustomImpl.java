package com.sparta.hirello.primary.progress.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.hirello.primary.board.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.hirello.primary.progress.entity.QProgress.progress;

@Repository
@RequiredArgsConstructor
public class ProgressRepositoryCustomImpl implements ProgressRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public void increaseOrderBetween(Board board, int startOrder, int endOrder) {
        queryFactory.update(progress)
                .set(progress.order, progress.order.add(1))
                .where(
                        progress.board.eq(board),
                        progress.order.between(startOrder, endOrder)
                )
                .execute();
    }

    @Override
    @Transactional
    public void decreaseOrderBetween(Board board, int startOrder, int endOrder) {
        queryFactory.update(progress)
                .set(progress.order, progress.order.subtract(1))
                .where(
                        progress.board.eq(board),
                        progress.order.between(startOrder, endOrder)
                )
                .execute();
    }

}