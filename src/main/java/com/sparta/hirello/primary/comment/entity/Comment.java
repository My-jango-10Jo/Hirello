package com.sparta.hirello.primary.comment.entity;

import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.primary.comment.dto.CommentRequest;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.secondary.base.entity.Timestamped;
import com.sparta.hirello.secondary.exception.DifferentCardException;
import com.sparta.hirello.secondary.exception.DifferentUserException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Comment(CommentRequest request, Card card, User user) {
        this.content = request.getContent();
        this.user = user;
        this.card = card;
    }

    public static Comment of(CommentRequest request, Card card, User user) {
        return new Comment(request, card, user);
    }

    public void verifyUser(Long userId) {
        if (!userId.equals(this.user.getId())) {
            throw new DifferentUserException();
        }
    }

    public void update(CommentRequest request) {
        this.content = request.getContent();
    }

    public void verifyCard(Long cardId) {
        if (!cardId.equals(this.card.getCardId())) {
            throw new DifferentCardException("해당 카드의 댓글이 아닙니다.");
        }
    }

}
