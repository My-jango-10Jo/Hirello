package com.sparta.hirello.primary.board.controller;

import com.sparta.hirello.primary.board.dto.request.BoardRequest;
import com.sparta.hirello.primary.board.dto.request.BoardUserRequest;
import com.sparta.hirello.primary.board.dto.request.BoardUserRoleRequest;
import com.sparta.hirello.primary.board.dto.response.BoardResponse;
import com.sparta.hirello.primary.board.dto.response.BoardUserResponse;
import com.sparta.hirello.primary.board.service.BoardService;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.sparta.hirello.secondary.util.ControllerUtil.getResponseEntity;

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
            @Valid @RequestBody BoardRequest requestDto
    ) {
        User user = userDetails.getUser();
        BoardResponse response = boardService.createBoard(user, requestDto);
        return getResponseEntity(response, "보드 생성 완료 ");
    }

    /**
     * 보드에 유저를 초대 합니다.
     *
     * @param userDetails 인가된 유저 정보
     * @param boardId     클라이언트에서 요청한 보드 생성 정보
     */
    @PostMapping("/{boardId}")
    public ResponseEntity<CommonResponse<?>> boardUserInvite(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardId,
            @RequestBody BoardUserRequest request) {
        User user = userDetails.getUser();
        BoardUserResponse response = boardService.boardUserInvite(user,
                boardId, request);
        return getResponseEntity(response, "초대 완료");//미구현
    }

    /**
     * 보드 목록들을 조회 합니다.
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getBoardList(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        List<BoardResponse> responseDtos = boardService.getBoardList(user);

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
            @Valid @RequestBody BoardRequest requestDto,
            @PathVariable Long boardId
    ) {
        User user = userDetails.getUser();
        BoardResponse responseDto = boardService.updateBoard(user, requestDto, boardId);

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
        User user = userDetails.getUser();
        boardService.deleteBoard(user, boardId);
        return getResponseEntity(1, "보드 삭제 완료");
    }

    /**
     * 보드 맴버 유저의 권한을 변경 합니다.
     *
     * @param userDetails   인가된 유저 정보
     * @param boardMemberId 삭제 해야할 보드 정보
     * @param request       변경할 유저의 ID,요청 권한 정보
     */
    @PatchMapping("/boardmembers/{boardMemberId}")
    public ResponseEntity<CommonResponse<?>> updateUserBoardRole(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long boardMemberId,
            @RequestBody BoardUserRoleRequest request
    ) {
        User user = userDetails.getUser();

        BoardUserResponse response = boardService.updateUserBoardRole(user, boardMemberId, request);

        return getResponseEntity(response, "권한 변경 완료");
    }
}
