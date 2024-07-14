package com.sparta.hirello.primary.card.dto.response;

import com.sparta.hirello.primary.card.entity.Card;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardResponse {

    private Long boardId;

    private Long cardId;
    private String title;
    private String description;
    private LocalDateTime deadlineAt;
    private String workerName;
    private Long progressId;

    private CardResponse(Card card) {
        this.boardId = card.getProgress().getBoard().getId();
        this.cardId = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.deadlineAt = card.getDeadlineAt();
        this.workerName = card.getWorker().getUsername();
        this.progressId = card.getProgress().getId();
    }

    public static CardResponse of(Card card) {
        return new CardResponse(card);
    }

}
