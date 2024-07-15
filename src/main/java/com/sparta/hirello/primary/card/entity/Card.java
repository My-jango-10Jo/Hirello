package com.sparta.hirello.primary.card.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.hirello.primary.card.dto.request.CardCreateRequest;
import com.sparta.hirello.primary.card.dto.request.CardUpdateRequest;
import com.sparta.hirello.primary.comment.entity.Comment;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.secondary.base.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Long id;

    private String title;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime deadline;

    @Column(name = "card_order")
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_id")
    private Progress progress;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private User worker;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    /**
     * 생성자
     */
    private Card(CardCreateRequest request, Progress progress, User worker) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.deadline = request.getDeadline();
        this.progress = progress;
        this.worker = worker;
    }

    public static Card of(CardCreateRequest request, Progress progress, User worker) {
        return new Card(request, progress, worker);
    }

    public void update(CardUpdateRequest request, User worker) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.deadline = request.getDeadline();
        this.worker = worker;
    }

    public void updateOrder(int order) {
        this.order = order;
    }

    public void updateProgress(Progress progress) {
        this.progress = progress;
    }

    public void updateWorker(User worker) {
        this.worker = worker;
    }

}
