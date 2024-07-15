package com.sparta.hirello.primary.card.dto.response;

import com.sparta.hirello.primary.card.entity.Card;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private int order;
    private Long workerId;
    private Long userId;
    private Long progressId;

    private CardResponse(Card card) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.deadline = card.getDeadline();
        this.progressId = card.getProgress().getId();
        this.workerId = card.getWorker().getId();
        this.userId = card.getUser().getId();
        this.order = card.getOrder();
    }

    public static CardResponse of(Card card) {
        return new CardResponse(card);
    }

}
