package com.sparta.hirello.primary.card.entity;

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

    private LocalDateTime deadline;

    @Column(name = "card_order")
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private User worker; // 작업자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 카드 생성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "progress_id")
    private Progress progress;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    /**
     * 생성자
     */
    private Card(CardCreateRequest request, Progress progress, User worker, User user) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.deadline = request.getDeadline();
        this.progress = progress;
        this.user = user;
        this.worker = worker;
        this.order = progress.getCards().size();
        progress.getCards().add(this);
    }

    public static Card of(CardCreateRequest request, Progress progress, User worker, User user) {
        return new Card(request, progress, worker, user);
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

}
