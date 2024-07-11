package com.sparta.hirello.primary.card.dto;

import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.column.entity.Columns;
import com.sparta.hirello.primary.user.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardResponse {

    Long cardId;
    String title;
    String description;
    LocalDateTime deadlineAt;
    User worker;
    Columns columns;

    private CardResponse(Card card) {
        this.cardId = card.getCardId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.deadlineAt = card.getDeadlineAt();
        this.worker = card.getWorker();
        this.columns = card.getColumns();
    }

    public static CardResponse of(Card card) {
        return new CardResponse(card);
    }
}
