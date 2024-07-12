package com.sparta.hirello.primary.comment.controller;

import com.sparta.hirello.primary.comment.dto.CommentRequest;
import com.sparta.hirello.primary.comment.dto.CommentResponse;
import com.sparta.hirello.primary.comment.entity.Comment;
import com.sparta.hirello.primary.comment.service.CommentService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.hirello.secondary.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommonResponse<?>> createComment(
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Comment comment = commentService.createComment(request, userDetails.getUser());
        return getResponseEntity(CommentResponse.of(comment), "댓글 작성 성공");
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommonResponse<?>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Comment comment = commentService.updateComment(commentId, request, userDetails.getUser());
        return getResponseEntity(CommentResponse.of(comment), "댓글 수정 성공");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse<?>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long response = commentService.deleteComment(commentId, userDetails.getUser());
        return getResponseEntity(response, "댓글 삭제 성공");
    }

}
