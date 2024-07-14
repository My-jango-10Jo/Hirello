package com.sparta.hirello.primary.progress.dto.response;

import com.sparta.hirello.primary.progress.entity.Progress;
import lombok.Data;

@Data
public class ProgressResponse {

    private Long id;
    private String progressName;
    private Long userId;

    private ProgressResponse(Progress progress){
        this.id=progress.getId();
        this.progressName=progress.getProgressName();
        this.userId=progress.getUser().getId();
    }

    public static ProgressResponse of(Progress progress) {
        return new ProgressResponse(progress);
    }
}
