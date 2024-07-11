package com.sparta.hirello.primary.board.controller;

import static com.sparta.hirello.secondary.util.ControllerUtil.getResponseEntity;

import com.sparta.hirello.primary.board.dto.request.BoardRequestDto;
import com.sparta.hirello.primary.board.dto.response.BoardResponseDto;
import com.sparta.hirello.primary.board.service.BoardService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
            @Valid @RequestBody BoardRequestDto requestDto
    ) {
        BoardResponseDto responseDto = boardService.createBoard(userDetails, requestDto);
        return getResponseEntity(responseDto, "보드 생성 완료 ");
    }

    /**
     * 보드 목록들을 조회 합니다.
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getBoardList() {
        List<BoardResponseDto> responseDtos = boardService.getBoardList();

        return getResponseEntity(responseDtos, "보드 목록 조회 완료");
    }

    /**
     * 보드 수정
     */
    @PutMapping("/{boardId}")
    public ResponseEntity<CommonResponse<?>> updateBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BoardRequestDto requestDto,
            @PathVariable Long boardId
    ) {
        BoardResponseDto responseDto = boardService.updateBoard(userDetails, requestDto, boardId);

        return getResponseEntity(responseDto, "보드 수정 완료");
    }

}
