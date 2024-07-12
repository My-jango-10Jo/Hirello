package com.sparta.hirello.primary.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {

    @NotNull
    private Long cardId;

    @NotBlank
    private String content;

}
