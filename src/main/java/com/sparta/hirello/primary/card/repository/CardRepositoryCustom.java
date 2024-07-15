package com.sparta.hirello.primary.card.repository;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardRepositoryCustom {

    Page<Card> findByBoard(Board board, Pageable pageable);

    Page<Card> findByBoardAndWorker(Board board, User user, Pageable pageable);

    Page<Card> findByBoardAndProgress(Board board, Progress progress, Pageable pageable);

    Page<Card> findByBoardAndWorkerAndProgress(Board board, User user, Progress progress, Pageable pageable);

    void decreaseOrderBetween(Progress progress, int startOrder, int endOrder);

    void increaseOrderBetween(Progress progress, int startOrder, int endOrder);

}
