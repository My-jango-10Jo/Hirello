package com.sparta.hirello.primary.card.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.hirello.primary.card.dto.request.CardUpdateRequest;
import com.sparta.hirello.primary.card.dto.request.CreateCardRequest;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.comment.entity.Comment;
import com.sparta.hirello.primary.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime deadlineAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private User worker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_id")
    private Progress progress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    @Builder
    private Card (String title, String description, LocalDateTime deadlineAt,
                  User worker, Progress progress, User user) {

        this.title = title;
        this.description = description;
        this.deadlineAt = deadlineAt;
        this.worker = worker;
        this.progress = progress;
        this.user = user;
    }

    public static Card of(CreateCardRequest requestDto,
                          User user, User worker, Progress progress) {
        return new Card (requestDto.getTitle(),requestDto.getDescription(), requestDto.getDeadlineAt(),
                worker, progress,user);
    }

    public Card updateCard(CardUpdateRequest request, User worker) {

        this.title = request.getTitle();

        if (request.getDescription() != null) {
            this.description = request.getDescription();
        }
        if (request.getWorkerName() != null) {
            this.worker = worker;
        }
        if (request.getDescription() != null) {
            this.deadlineAt = request.getDeadlineAt();
        }

        return this;
    }

    public Card updateCardProgress(Progress progress) {
        this.progress = progress;

        return this;
    }

}
