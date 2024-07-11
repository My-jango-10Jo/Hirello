package com.sparta.hirello.primary.card.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.card.dto.request.CreateCardRequest;
import com.sparta.hirello.primary.column.entity.Columns;
import com.sparta.hirello.primary.comment.entity.Comment;
import com.sparta.hirello.primary.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
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

    @OneToMany(mappedBy = "card")
    private List<Comment> commentList;

    @Builder
    public Card(String title, String description, LocalDateTime deadlineAt,
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
        return Card.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .deadlineAt(requestDto.getDeadlineAt())
                .worker(worker)
                .columns(column)
                .user(user)
                .build();
    }
}
