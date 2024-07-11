package com.sparta.hirello.primary.card.dto.response;

import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.column.entity.Columns;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CardOfColumnResponse {

    private Long boardId;

    private Long cardId;
    private String title;
    private String description;
    private LocalDateTime deadlineAt;
    private String workerName;
    private Columns columns;

    private final List<CardResponse> cardOfColumnList;

    public static CardOfColumnResponse of(List<Card> checkedCardList) {
        List<CardResponse> cardOfColumnList = checkedCardList.stream()
                .map(CardResponse::of).toList();

        return CardOfColumnResponse.builder()
                .cardOfColumnList(cardOfColumnList)
                .build();
    }
}
