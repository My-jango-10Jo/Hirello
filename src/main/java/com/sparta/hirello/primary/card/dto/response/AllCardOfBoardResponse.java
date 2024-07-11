package com.sparta.hirello.primary.card.dto.response;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.card.dto.response.CardResponse;
import com.sparta.hirello.primary.column.entity.Columns;
import com.sparta.hirello.primary.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class AllCardOfBoardResponse {

    private Long boardId;

    private Long cardId;
    private String title;
    private String description;
    private LocalDateTime deadlineAt;
    private User worker;
    private Columns columns;

    private final List<CardResponse> cardOfBoardList;

    public static AllCardOfBoardResponse of(Board board) {

        List<CardResponse> cardResponseList = board.getColumnsList().stream()
                .flatMap(columns -> columns.getCardList().stream())
                .map(CardResponse::of)
                .collect(Collectors.toList());

        return AllCardOfBoardResponse.builder()
                .boardId(board.getBoardId())
                .cardOfBoardList(cardResponseList)
                .build();
    }
}
