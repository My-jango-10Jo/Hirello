package com.sparta.hirello.primary.board.controller;

import static com.sparta.hirello.secondary.util.ControllerUtil.getResponseEntity;

import com.sparta.hirello.primary.board.dto.CreateBoardRequestDto;
import com.sparta.hirello.primary.board.dto.CreateBoardResponseDto;
import com.sparta.hirello.primary.board.service.BoardService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * 보드를 생성 합니다.
     *
     * @param userDetails 인가된 유저 정보
     * @param requestDto  클라이언트에서 요청한 유저 생성 정보
     */

    @PostMapping
    public ResponseEntity<CommonResponse<?>> createBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CreateBoardRequestDto requestDto
    ) {
        CreateBoardResponseDto responseDto = boardService.createBoard(userDetails, requestDto);
        return getResponseEntity(responseDto, "보드 생성이 완료 되었습니다.");
    }


}
