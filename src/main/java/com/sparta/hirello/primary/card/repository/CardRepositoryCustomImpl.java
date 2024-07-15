package com.sparta.hirello.primary.card.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CardRepositoryCustomImpl implements CardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Card> findByBoard(Board board, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Card> findByBoardAndWorker(Board board, User user, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Card> findByBoardAndProgress(Board board, Progress progress, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Card> findByBoardAndWorkerAndProgress(Board board, User user, Progress progress, Pageable pageable) {
        return null;
    }

    @Override
    public void decreaseOrderBetween(Progress progress, int startOrder, int endOrder) {

    }

    @Override
    public void increaseOrderBetween(Progress progress, int startOrder, int endOrder) {

    }

}
