package com.sparta.hirello.primary.card.dto.response;

import com.sparta.hirello.primary.card.entity.Card;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CardOfProgressResponse {

    private Long boardId;

    private Long cardId;
    private String title;
    private String description;
    private LocalDateTime deadlineAt;
    private String workerName;
    private Long columId;

    private final List<CardResponse> cardOfProgressList;


    public static CardOfProgressResponse of(List<Card> checkedCardList) {
        List<CardResponse> cardOfProgressList = checkedCardList.stream()
                .map(CardResponse::of).toList();

        return CardOfProgressResponse.builder()
                .cardOfProgressList(cardOfProgressList)
                .build();
    }
}
