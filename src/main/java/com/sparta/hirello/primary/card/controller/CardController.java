package com.sparta.hirello.primary.card.controller;

import com.sparta.hirello.primary.card.dto.request.CardCreateRequest;
import com.sparta.hirello.primary.card.dto.request.CardMoveOrderRequest;
import com.sparta.hirello.primary.card.dto.request.CardUpdateRequest;
import com.sparta.hirello.primary.card.dto.response.CardResponse;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.service.CardService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.hirello.secondary.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    /**
     * 카드 생성
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createCard(
            @Valid @RequestBody CardCreateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Card card = cardService.createCard(request, userDetails.getUser());
        return getResponseEntity(CardResponse.of(card), "카드 생성 성공");
    }

    /**
     * 해당 보드의 카드 목록 조회 - 작업자별, 프로그레스별
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getCards(
            @RequestParam Long boardId,
            @RequestParam(required = false) Long workerId,
            @RequestParam(required = false) Long progressId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable
    ) {
        Page<Card> page = cardService.getCards(boardId, workerId, progressId, userDetails.getUser(), pageable);
        Page<CardResponse> response = page.map(CardResponse::of);
        return getResponseEntity(response, "카드 목록 조회 성공");
    }

    /**
     * 카드 이동
     */
    @PatchMapping("/{cardId}/move-order")
    public ResponseEntity<CommonResponse<?>> moveCard(
            @PathVariable Long cardId,
            @Valid @RequestBody CardMoveOrderRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Card card = cardService.moveCard(cardId, request, userDetails.getUser());
        return getResponseEntity(CardResponse.of(card), "카드 이동 성공");
    }

    /**
     * 카드 수정
     */
    @PatchMapping("/{cardId}")
    public ResponseEntity<CommonResponse<?>> updateCard(
            @PathVariable Long cardId,
            @Valid @RequestBody CardUpdateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Card card = cardService.updateCard(cardId, request, userDetails.getUser());
        return getResponseEntity(CardResponse.of(card), "카드 수정 성공");
    }

    /**
     * 카드 삭제
     */
    @DeleteMapping("/{cardId}")
    public ResponseEntity<CommonResponse<?>> deleteCard(
            @PathVariable Long cardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.deleteCard(cardId, userDetails.getUser());
        return getResponseEntity("카드 삭제 성공");
    }

}
