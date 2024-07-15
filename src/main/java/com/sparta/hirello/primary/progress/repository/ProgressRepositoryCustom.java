package com.sparta.hirello.primary.progress.repository;

import com.sparta.hirello.primary.board.entity.Board;

public interface ProgressRepositoryCustom {

    void increaseOrderBetween(Board board, int startOrder, int endOrder);

    void decreaseOrderBetween(Board board, int startOrder, int endOrder);

}