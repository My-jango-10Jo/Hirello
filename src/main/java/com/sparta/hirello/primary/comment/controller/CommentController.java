package com.sparta.hirello.primary.comment.controller;

import com.sparta.hirello.primary.comment.dto.CommentRequest;
import com.sparta.hirello.primary.comment.dto.CommentResponse;
import com.sparta.hirello.primary.comment.service.CommentService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comment API", description = "댓글 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cards/{cardId}/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "입력된 boardId와 CardId에 댓글을 생성합니다.")
    @PostMapping()
    public ResponseEntity<CommonResponse> createComment(@PathVariable Long cardId,
                                                        @PathVariable Long boardId,
                                                        @RequestBody CommentRequest request,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(cardId, boardId, request, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .data("댓글이 생성되었습니다")
                        .build());

    }

    @Operation(summary = "댓글 수정", description = "작성자와 일치하면 commentId 기준으로 댓글을 수정합니다.")
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable Long commentId,
                                                        @PathVariable Long boardId,
                                                        @RequestBody CommentRequest request,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.updateComment(commentId, boardId, request, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(
                CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data("댓글이 수정되었습니다")
                        .build());
    }

    @Operation(summary = "댓글 삭제", description = "작성자와 일치하면 commentId 기준으로 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable Long commentId,
                                                        @PathVariable Long boardId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, boardId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(
                CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data("댓글이 삭제되었습니다")
                        .build());
    }

    @Operation(summary = "댓글 조회", description = "cardId와 일치하는 댓글 목록을 불러옵니다.")
    @GetMapping()
    public List<CommentResponse> getComments(
            @PathVariable Long cardId) {
        return commentService.getComments(cardId);
    }
}
