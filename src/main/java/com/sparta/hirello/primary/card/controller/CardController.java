package com.sparta.hirello.primary.card.controller;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.card.dto.request.CardDeleteRequest;
import com.sparta.hirello.primary.card.dto.request.CardUpdateOnlyProgressRequest;
import com.sparta.hirello.primary.card.dto.request.CardUpdateRequest;
import com.sparta.hirello.primary.card.dto.request.CreateCardRequest;
import com.sparta.hirello.primary.card.dto.response.*;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.service.CardService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
     * Card 조회 - 보드별, 컬럼별, 작업자별
     *
     * @param userDetails
     * @param boardId
     * @param workerId
     * @param columnId
     * @return List<Card>
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getCards(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(required = true) Long boardId,
            @RequestParam(required = false) Long workerId,
            @RequestParam(required = false) Long columnId
    ) {
        if (columnId == null && workerId == null) {
            Board checkedBoard = cardService.getAllCardOfBoard(userDetails.getUser(), boardId);
            return getResponseEntity(AllCardOfBoardResponse.of(checkedBoard), "조회 성공");
        }

        if (columnId == null) {
            List<Card> checkedCardList = cardService.getCardOfSpecificWorker(userDetails.getUser(), boardId, workerId);
            return getResponseEntity(CardOfProgressResponse.of(checkedCardList), "조회 성공");
        }

        if (workerId == null) {
            List<Card> checkedCardList = cardService.getCardOfColumn(userDetails.getUser(), boardId, columnId);
            return getResponseEntity(CardOfSpecificWorkerResponse.of(checkedCardList), "조회 성공");
        }


        CommonResponse<?> errorResponse = CommonResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .msg("잘못된 접근입니다.")
                .data(null)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
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
            @Valid @RequestBody final CardUpdateOnlyProgressRequest request
    ) {
        Card updatedCard = cardService.updateCardProgress(userDetails.getUser(), cardId, request);
        return getResponseEntity(CardUpdateResponse.of(updatedCard), "수정 성공");
    }

    /**
     * Card 삭제
     *
     * @param userDetails
     * @param cardId
     * @param request
     */
    @DeleteMapping("/cloumn/{cardId}")
    public ResponseEntity<CommonResponse<?>> deleteCard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cardId,
            @Valid @RequestBody final CardDeleteRequest request
    ) {
        cardService.deleteCard(userDetails.getUser(), cardId, request);
        return getResponseEntity("카드 삭제 완료");
    }


}
