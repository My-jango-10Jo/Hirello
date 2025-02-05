package com.sparta.hirello.primary.card.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardUpdateRequest {

    private String title;

    private String description;

    private LocalDateTime deadline;

    private Long workerId;

}
