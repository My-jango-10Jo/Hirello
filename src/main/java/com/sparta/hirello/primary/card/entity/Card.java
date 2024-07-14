package com.sparta.hirello.primary.card.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
<<<<<<< Updated upstream
import com.sparta.hirello.primary.card.dto.request.CardUpdateRequest;
=======
>>>>>>> Stashed changes
import com.sparta.hirello.primary.card.dto.request.CreateCardRequest;
import com.sparta.hirello.primary.column.entity.Columns;
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
    private Long cardId;

    private Long orderNumber;

    @Column(nullable = false)
    private String title;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime deadlineAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id")
    private User worker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id")
    private Columns columns;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    @Builder
    private Card (String title, String description, LocalDateTime deadlineAt,
                User worker, Columns columns, User user) {
        this.title = title;
        this.description = description;
        this.deadlineAt = deadlineAt;
        this.worker = worker;
        this.columns = columns;
        this.user = user;
    }

    public static Card of(CreateCardRequest requestDto,
                          User user, User worker, Columns column) {
        return new Card (requestDto.getTitle(),requestDto.getDescription(), requestDto.getDeadlineAt(),
                worker, column,user);
    }
}
