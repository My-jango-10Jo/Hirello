package com.sparta.hirello.primary.progress.dto.response;

import com.sparta.hirello.primary.progress.entity.Progress;
import lombok.Data;

@Data
public class ProgressResponse {

    private Long id;
    private String title;
    private int order;
    private Long boardId;

    private ProgressResponse(Progress progress) {
        this.id = progress.getId();
        this.title = progress.getTitle();
        this.order = progress.getOrder();
        this.boardId = progress.getBoard().getId();
    }

    public static ProgressResponse of(Progress progress) {
        return new ProgressResponse(progress);
    }

}
