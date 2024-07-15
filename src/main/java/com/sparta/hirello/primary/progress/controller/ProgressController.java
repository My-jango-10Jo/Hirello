package com.sparta.hirello.primary.progress.controller;

import com.sparta.hirello.primary.progress.dto.request.ProgressCreateRequest;
import com.sparta.hirello.primary.progress.dto.request.ProgressMoveRequest;
import com.sparta.hirello.primary.progress.dto.response.ProgressResponse;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.progress.service.ProgressService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<CommonResponse<?>> createColumn(
            @Valid @RequestBody ProgressCreateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Progress progress = progressService.createProgress(request, userDetails.getUser());
        return getResponseEntity(ProgressResponse.of(progress), "프로그레스 생성 성공");
    }

    /**
     * 프로그레스 순서 이동
     */
    @PatchMapping("/{progressId}")
    public ResponseEntity<CommonResponse<?>> moveProgress(
            @PathVariable Long progressId,
            @Valid @RequestBody ProgressMoveRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Progress progress = progressService.moveProgress(progressId, request, userDetails.getUser());
        return getResponseEntity(ProgressResponse.of(progress), "프로그레스 생성 성공");
    }

    /**
     * 프로그레스 삭제
     */
    @DeleteMapping("/{progressId}")
    public ResponseEntity<CommonResponse<?>> deleteColumn(
            @PathVariable Long progressId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        progressService.deleteProgress(progressId, userDetails.getUser());
        return getResponseEntity(null, "프로그레스 삭제 완료");
    }

}
