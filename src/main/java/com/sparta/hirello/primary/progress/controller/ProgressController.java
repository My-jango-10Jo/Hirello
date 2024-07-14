package com.sparta.hirello.primary.progress.controller;

import com.sparta.hirello.primary.progress.dto.request.ProgressRequest;
import com.sparta.hirello.primary.progress.dto.response.ProgressResponse;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.progress.service.ProgressService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.hirello.secondary.util.ControllerUtil.getResponseEntity;

@RequestMapping("/api/columns")
@RestController
@RequiredArgsConstructor
public class ProgressController {
    private final ProgressService progressService;

    @PostMapping
    public ResponseEntity<CommonResponse<?>> createProgress(ProgressRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Progress progress = progressService.createProgress(request, userDetails.getUser());
        return getResponseEntity(ProgressResponse.of(progress), "컬럼 생성 성공") ;
    }

    @DeleteMapping("/{columnId}")
    public ResponseEntity<CommonResponse<?>> deleteProgress(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        progressService.deleteProgress(id, userDetails.getUser());
        return getResponseEntity(id, "컬럼 삭제 완료");
    }
}
