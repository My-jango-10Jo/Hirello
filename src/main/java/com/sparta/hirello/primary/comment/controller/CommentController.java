package com.sparta.hirello.primary.comment.controller;

import com.sparta.hirello.primary.comment.dto.CommentRequest;
import com.sparta.hirello.primary.comment.service.CommentService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/cards/{cardId}/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public CommonResponse createComment(@PathVariable Long id,
                                        @RequestBody CommentRequest request,
                                        UserDetailsImpl userDetails) {
        commentService.createComment(id,request,userDetails.getUser());

        return CommonResponse.builder()
                .statusCode(201)
                .build();
    }

    @PatchMapping("/{commentId}")
    public CommonResponse updateComment(@PathVariable Long cardId,
                                        @PathVariable Long commentId,
                                        @RequestBody CommentRequest request,
                                        UserDetailsImpl userDetails) {
        commentService.updateComment(cardId,commentId,request,userDetails.getUser());

        return CommonResponse.builder()
                .statusCode(204)
                .build();
    }

    @DeleteMapping("/{commentId}")
    public CommonResponse deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);


        return CommonResponse.builder()
                .statusCode(204)
                .build();
    }

}
