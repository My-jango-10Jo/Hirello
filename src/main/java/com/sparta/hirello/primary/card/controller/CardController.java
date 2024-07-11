package com.sparta.hirello.primary.card.controller;

import com.sparta.hirello.primary.card.dto.request.CreateCardRequest;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.card.service.CardService;
import com.sparta.hirello.primary.user.dto.response.UserResponse;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CardController {

    private final CardService cardService;

    @PostMapping("/card")
    public ResponseEntity<CommonResponse<Object>> createCard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody final CreateCardRequest request
            ) {
        Card newCard = cardService.createCard(userDetails, request);
        return getResponseEntity(CardResponse.of(newCard), "등록 성공");//임시
    }

}
