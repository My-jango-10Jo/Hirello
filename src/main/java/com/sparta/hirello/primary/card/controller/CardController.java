package com.sparta.hirello.primary.card.controller;

import com.sparta.hirello.primary.card.dto.request.CreateCardRequest;
import com.sparta.hirello.primary.user.dto.response.UserResponse;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sparta.hirello.primary.user.entity.QUser.user;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CardController {

//    public ResponseEntity<CommonResponse<Object>> postCard(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @Valid @RequestBody final CreateCardRequest request
//            ) {
//        return ResponseEntity(UserResponse.of,"ㅇㄻㄴ");//임시
//    }

}
