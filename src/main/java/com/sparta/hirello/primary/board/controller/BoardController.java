package com.sparta.hirello.primary.board.controller;

import static com.sparta.hirello.secondary.util.ControllerUtil.getResponseEntity;

import com.sparta.hirello.primary.board.dto.request.BoardRequestDto;
import com.sparta.hirello.primary.board.dto.response.BoardResponseDto;
import com.sparta.hirello.primary.board.service.BoardService;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
     * @param requestDto  클라이언트에서 요청한 보드 생성 정보
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
     * 보드에 유저를 초대 합니다.
     *
     * @param userDetails 인가된 유저 정보
     * @param boardId     클라이언트에서 요청한 보드 생성 정보
     */
    @PostMapping("/{boardId}")
    public ResponseEntity<CommonResponse<?>> boardUserVisit(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId) {
        User user = boardService.boardUserVisit(userDetails.getUser(), boardId);
        return null;//미구현
    }

    /**
     * 보드 목록들을 조회 합니다.
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getBoardList(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BoardResponseDto> responseDtos = boardService.getBoardList(userDetails.getUser());

        return getResponseEntity(responseDtos, "보드 목록 조회 완료");
    }

    /**
     * 보드 수정
     *
     * @param userDetails 인가된 유저 정보
     * @param requestDto  클라이언트에서 요청한 보드 수정 정보
     * @param boardId     수정 해야할 보드 정보
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

    /**
     * 보드 삭제
     *
     * @param userDetails 인가된 유저 정보
     * @param boardId     삭제 해야할 보드 정보
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<CommonResponse<?>> deleteBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId
    ) {
        boardService.deleteBoard(userDetails, boardId);
        return getResponseEntity(1, "보드 삭제 완료");
    }

}
