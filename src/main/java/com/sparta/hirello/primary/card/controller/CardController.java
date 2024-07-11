package com.sparta.hirello.primary.card.controller;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.card.dto.request.CreateCardRequest;
import com.sparta.hirello.primary.card.dto.response.AllCardOfBoardResponse;
import com.sparta.hirello.primary.card.dto.response.CardResponse;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.service.CardService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
     * @return
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createCard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody final CreateCardRequest request
    ) {
        Card newCard = cardService.createCard(userDetails, request);
        return getResponseEntity(CardResponse.of(newCard), "등록 성공");
    }


    /**
     * Board 의 Card 전체 조회
     * @param userDetails
     * @param boardId
     * @return
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<CommonResponse<?>> getAllCardOfBoard (
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @PathVariable final Long boardId
    ){
        Board checkedBoard = cardService.getAllCardOfBoard(userDetails, boardId);

        return getResponseEntity(AllCardOfBoardResponse.of(checkedBoard), "조회 성공");

    }


}
