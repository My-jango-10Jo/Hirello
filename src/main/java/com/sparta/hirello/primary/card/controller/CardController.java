package com.sparta.hirello.primary.card.controller;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.card.dto.request.CardOfSpecificWorkerRequest;
import com.sparta.hirello.primary.card.dto.request.CreateCardRequest;
import com.sparta.hirello.primary.card.dto.response.AllCardOfBoardResponse;
import com.sparta.hirello.primary.card.dto.response.CardOfSpecificWorkerResponse;
import com.sparta.hirello.primary.card.dto.response.CardResponse;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.service.CardService;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * 카드 생성
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
     * @param userDetails
     * @param boardId
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<CommonResponse<?>> getAllCardOfBoard (
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable final Long boardId
    ){
        Board checkedBoard = cardService.getAllCardOfBoard(userDetails.getUser(), boardId);
        return getResponseEntity(AllCardOfBoardResponse.of(checkedBoard), "조회 성공");

    }

    /**
     * 작업자별 Card 조회
     * @param userDetails
     * @param request
     * @return
     */
    @GetMapping("/workers")
    public ResponseEntity<CommonResponse<?>> getCardOfSpecificWorker (
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody final CardOfSpecificWorkerRequest request
    ){
        List<Card> checkedCardList = cardService.getCardOfSpecificWorker(userDetails.getUser(), request);
        return getResponseEntity(CardOfSpecificWorkerResponse.of(checkedCardList), "조회 성공");
    }




}
