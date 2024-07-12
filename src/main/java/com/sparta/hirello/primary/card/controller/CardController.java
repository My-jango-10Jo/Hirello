package com.sparta.hirello.primary.card.controller;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.card.dto.request.*;
import com.sparta.hirello.primary.card.dto.response.*;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.service.CardService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.hirello.secondary.util.ControllerUtil.getResponseEntity;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    /**
     * Card 생성
     *
     * @param userDetails
     * @param request
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createCard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody final CreateCardRequest request
    ) {
        Card newCard = cardService.createCard(userDetails.getUser(), request);
        return getResponseEntity(CardResponse.of(newCard), "등록 성공");
    }


    /**
     * Board 의 Card 전체 조회
     *
     * @param userDetails
     * @param boardId
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<CommonResponse<?>> getAllCardOfBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable final Long boardId
    ) {
        Board checkedBoard = cardService.getAllCardOfBoard(userDetails.getUser(), boardId);
        return getResponseEntity(AllCardOfBoardResponse.of(checkedBoard), "조회 성공");

    }

    /**
     * Worker 별 Card 조회
     *
     * @param userDetails
     * @param request
     */
    @GetMapping("/workers")
    public ResponseEntity<CommonResponse<?>> getCardOfSpecificWorker(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody final CardOfSpecificWorkerRequest request
    ) {
        List<Card> checkedCardList = cardService.getCardOfSpecificWorker(userDetails.getUser(), request);
        return getResponseEntity(CardOfSpecificWorkerResponse.of(checkedCardList), "조회 성공");
    }

    /**
     * Column 별 Card 조회
     *
     * @param userDetails
     * @param request
     */
    @GetMapping("/column")
    public ResponseEntity<CommonResponse<?>> getCardOfColumn(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody final CardOfColumnRequest request
    ) {
        List<Card> checkedCardList = cardService.getCardOfColumn(userDetails.getUser(), request);
        return getResponseEntity(CardOfColumnResponse.of(checkedCardList), "조회 성공");
    }

    /**
     * Card 내용 수정
     *
     * @param userDetails
     * @param cardId
     * @param request
     */
    @PatchMapping("/{cardId}")
    public ResponseEntity<CommonResponse<?>> updateCard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cardId,
            @Valid @RequestBody final CardUpdateRequest request
    ) {
        Card updatedCard = cardService.updateCard(userDetails.getUser(), cardId, request);
        return getResponseEntity(CardUpdateResponse.of(updatedCard), "수정 성공");
    }

    /**
     * Card Column 만 수정
     *
     * @param userDetails
     * @param cardId
     * @param request
     */
    @PatchMapping("/cloumn/{cardId}")
    public ResponseEntity<CommonResponse<?>> updateCardColumn(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cardId,
            @Valid @RequestBody final CardUpdateOnlyColumnRequest request
    ) {
        Card updatedCard = cardService.updateCardColumn(userDetails.getUser(), cardId, request);
        return getResponseEntity(CardUpdateResponse.of(updatedCard), "수정 성공");
    }

    @DeleteMapping("/cloumn/{cardId}")
    public ResponseEntity<CommonResponse<?>> deleteCard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cardId,
            @Valid @RequestBody final CardDeleteRequest request
    ) {
        cardService.deleteCard(userDetails.getUser(), cardId, request);
        return getResponseEntity( "카드 삭제 완료");
    }


}
