package com.sparta.hirello.primary.board.controller;

import com.sparta.hirello.primary.board.dto.request.BoardMemberRequest;
import com.sparta.hirello.primary.board.dto.request.BoardRequest;
import com.sparta.hirello.primary.board.dto.response.BoardMemberResponse;
import com.sparta.hirello.primary.board.dto.response.BoardResponse;
import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.board.entity.BoardMember;
import com.sparta.hirello.primary.board.service.BoardService;
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
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    /**
     * 보드 생성
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createBoard(
            @Valid @RequestBody BoardRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Board board = boardService.createBoard(request, userDetails.getUser());
        return getResponseEntity(BoardResponse.of(board), "보드 생성 성공");
    }

    /**
     * 사용자 보드(자신이 생성한 보드이거나 초대된 보드) 목록 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getBoardList(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable
    ) {
        Page<Board> page = boardService.getUserBoards(userDetails.getUser(), pageable);
        Page<BoardResponse> response = page.map(BoardResponse::of);
        return getResponseEntity(response, "사용자 보드 목록 조회 성공");
    }

    /**
     * 보드 수정
     */
    @PutMapping("/{boardId}")
    public ResponseEntity<CommonResponse<?>> updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody BoardRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Board board = boardService.updateBoard(boardId, request, userDetails.getUser());
        return getResponseEntity(BoardResponse.of(board), "보드 수정 성공");
    }

    /**
     * 보드 삭제
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<CommonResponse<?>> deleteBoard(
            @PathVariable Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        //Long response = boardService.deleteBoard(boardId, userDetails.getUser());
        boardService.deleteBoard(boardId, userDetails.getUser());
        return getResponseEntity(boardId, "보드 삭제 성공");
    }

    /**
     * 보드 맴버 초대
     */
    @PostMapping("/{boardId}/invite-user")
    public ResponseEntity<CommonResponse<?>> inviteUser(
            @PathVariable Long boardId,
            @RequestBody BoardMemberRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardMember boardMember = boardService.inviteUser(boardId, request, userDetails.getUser());
        return getResponseEntity(BoardMemberResponse.of(boardMember), "보드 맴버 초대 성공");
    }

    /**
     * 보드 멤버 권한 변경
     */
    @PatchMapping("/{boardId}/update-user")
    public ResponseEntity<CommonResponse<?>> updateUserBoardRole(
            @PathVariable Long boardId,
            @RequestBody BoardMemberRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardMember boardMember = boardService.updateBoardMemberRole(boardId, request, userDetails.getUser());
        return getResponseEntity(BoardMemberResponse.of(boardMember), "보드 멤버 권한 변경 성공");
    }

}
