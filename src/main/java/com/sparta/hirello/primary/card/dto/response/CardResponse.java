package com.sparta.hirello.primary.card.dto.response;

import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.column.entity.Columns;
import com.sparta.hirello.primary.user.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardResponse {

    private Long cardId;
    private String title;
    private String description;
    private LocalDateTime deadlineAt;
    private String workerName;
    private Columns columns;

    private CardResponse(Card card) {
        this.cardId = card.getCardId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.deadlineAt = card.getDeadlineAt();
        this.workerName = card.getWorker().getUsername();
        this.columns = card.getColumns();
    }

    public static CardResponse of(Card card) {
        return new CardResponse(card);
    }

}
