package com.sparta.hirello.primary.comment.entity;

import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.column.entity.Columns;
import com.sparta.hirello.primary.comment.dto.CommentRequest;
import com.sparta.hirello.primary.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreatedDate
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id", nullable = false)
    private Columns columns;

    public Comment(CommentRequest request, Card card, User user) {
        this.content = request.getContent();
        this.user = user;
        this.card = card;
    }

    public void update(CommentRequest requestDto) {
        this.content = requestDto.getContent();
    }
}
