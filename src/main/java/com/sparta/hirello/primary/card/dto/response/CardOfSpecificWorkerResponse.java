package com.sparta.hirello.primary.card.dto.response;

import com.sparta.hirello.primary.card.entity.Card;
<<<<<<< Updated upstream
=======
import com.sparta.hirello.primary.column.entity.Columns;
>>>>>>> Stashed changes
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CardOfSpecificWorkerResponse {

    private Long boardId;

    private Long cardId;
    private String title;
    private String description;
    private LocalDateTime deadlineAt;
    private String workerName;
    private Long columnId;

    private final List<CardResponse> cardOfWorkerList;

    public static CardOfSpecificWorkerResponse of(List<Card> checkedCardList) {
        List<CardResponse> cardResponseList = checkedCardList.stream()
                .map(CardResponse::of).toList();

        return CardOfSpecificWorkerResponse.builder()
                .cardOfWorkerList(cardResponseList)
                .build();
    }
}
