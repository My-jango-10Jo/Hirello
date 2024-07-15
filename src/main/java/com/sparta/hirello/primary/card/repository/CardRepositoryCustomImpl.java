package com.sparta.hirello.primary.card.repository;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.sparta.hirello.primary.card.entity.QCard.card;
import static com.sparta.hirello.secondary.util.RepositoryUtil.getTotal;

@Repository
@RequiredArgsConstructor
public class CardRepositoryCustomImpl implements CardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Card> findByBoard(Board board, Pageable pageable) {
        // 데이터 조회 쿼리
        List<Card> cards = queryFactory
                .selectFrom(card)
                .where(card.progress.board.eq(board))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        Long totalCount = queryFactory
                .select(Wildcard.count)
                .from(card)
                .where(card.progress.board.eq(board))
                .fetchOne();

        return new PageImpl<>(cards, pageable, getTotal(totalCount));
    }

    @Override
    public Page<Card> findByBoardAndWorker(Board board, User worker, Pageable pageable) {
        // 데이터 조회 쿼리
        List<Card> cards = queryFactory
                .selectFrom(card)
                .where(
                        card.progress.board.eq(board),
                        card.worker.eq(worker)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        Long totalCount = queryFactory
                .select(Wildcard.count)
                .from(card)
                .where(
                        card.progress.board.eq(board),
                        card.worker.eq(worker)
                )
                .fetchOne();

        return new PageImpl<>(cards, pageable, getTotal(totalCount));

    }

    @Override
    public Page<Card> findByBoardAndProgress(Board board, Progress progress, Pageable pageable) {
        // 데이터 조회 쿼리
        List<Card> cards = queryFactory
                .selectFrom(card)
                .where(
                        card.progress.board.eq(board),
                        card.progress.eq(progress)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        Long totalCount = queryFactory
                .select(Wildcard.count)
                .from(card)
                .where(
                        card.progress.board.eq(board),
                        card.progress.eq(progress)
                )
                .fetchOne();

        return new PageImpl<>(cards, pageable, getTotal(totalCount));
    }

    @Override
    public Page<Card> findByBoardAndWorkerAndProgress(Board board, User worker, Progress progress, Pageable pageable) {
        // 데이터 조회 쿼리
        List<Card> cards = queryFactory
                .selectFrom(card)
                .where(
                        card.progress.board.eq(board),
                        card.worker.eq(worker),
                        card.progress.eq(progress)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트 쿼리
        Long totalCount = queryFactory
                .select(Wildcard.count)
                .from(card)
                .where(
                        card.progress.board.eq(board),
                        card.worker.eq(worker),
                        card.progress.eq(progress)
                )
                .fetchOne();

        return new PageImpl<>(cards, pageable, getTotal(totalCount));
    }

    @Override
    @Transactional
    public void increaseOrderBetween(Progress progress, int startOrder, int endOrder) {
        queryFactory.update(card)
                .set(card.order, card.order.add(1))
                .where(
                        card.progress.eq(progress),
                        card.order.between(startOrder, endOrder)
                )
                .execute();
    }

    @Override
    @Transactional
    public void decreaseOrderBetween(Progress progress, int startOrder, int endOrder) {
        queryFactory.update(card)
                .set(card.order, card.order.subtract(1))
                .where(
                        card.progress.eq(progress),
                        card.order.between(startOrder, endOrder)
                )
                .execute();
    }

}
