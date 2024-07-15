package com.sparta.hirello.primary.progress.controller;

import com.sparta.hirello.primary.progress.dto.request.ProgressCreateRequest;
import com.sparta.hirello.primary.progress.dto.request.ProgressMoveOrderRequest;
import com.sparta.hirello.primary.progress.dto.response.ProgressResponse;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.progress.service.ProgressService;
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
@RequestMapping("/progresses")
public class ProgressController {

    private final ProgressService progressService;

    /**
     * 프로그레스 생성
     */
    @PostMapping
    public ResponseEntity<CommonResponse<?>> createProgress(
            @Valid @RequestBody ProgressCreateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Progress progress = progressService.createProgress(request, userDetails.getUser());
        return getResponseEntity(ProgressResponse.of(progress), "프로그레스 생성 성공");
    }

    /**
     * 해당 보드의 프로그레스 목록 조회
     */
    @GetMapping
    public ResponseEntity<CommonResponse<?>> getBoardProgresses(
            @RequestParam Long boardId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Pageable pageable
    ) {
        Page<Progress> page = progressService.getBoardProgresses(boardId, userDetails.getUser(), pageable);
        Page<ProgressResponse> response = page.map(ProgressResponse::of);
        return getResponseEntity(response, "프로그레스 목록 조회 성공");
    }

    /**
     * 프로그레스 순서 이동
     */
    @PatchMapping("/{progressId}/move-order")
    public ResponseEntity<CommonResponse<?>> moveProgress(
            @PathVariable Long progressId,
            @Valid @RequestBody ProgressMoveOrderRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Progress progress = progressService.moveProgress(progressId, request, userDetails.getUser());
        return getResponseEntity(ProgressResponse.of(progress), "프로그레스 순서 이동 성공");
    }

    /**
     * 프로그레스 삭제
     */
    @DeleteMapping("/{progressId}")
    public ResponseEntity<CommonResponse<?>> deleteProgress(
            @PathVariable Long progressId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        progressService.deleteProgress(progressId, userDetails.getUser());
        return getResponseEntity(null, "프로그레스 삭제 완료");
    }

}
