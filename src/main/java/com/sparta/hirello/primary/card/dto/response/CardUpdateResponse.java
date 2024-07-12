package com.sparta.hirello.primary.card.dto.response;

import com.sparta.hirello.primary.card.entity.Card;

import java.time.LocalDateTime;

public class CardUpdateResponse {

    Long boardId;

    String title;
    String description;
    String workerName;
    String columnName;
    LocalDateTime deadlineAt;

    private CardUpdateResponse(Card card) {
        this.boardId = card.getColumns().getBoard().getBoardId();

        this.title = card.getTitle();
        this.description = card.getDescription();
        this.workerName = card.getWorker().getUsername();
        this.columnName = card.getColumns().getColumnName();
        this.deadlineAt = card.getDeadlineAt();

    }

    public static CardUpdateResponse of(Card updatedCard) {
        return new CardUpdateResponse(updatedCard);
    }
}
