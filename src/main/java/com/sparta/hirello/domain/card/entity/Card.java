package com.sparta.hirello.domain.card.entity;

import com.sparta.hirello.domain.comment.entity.Comment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.sparta.hirello.domain.column.entity.Columns;
import com.sparta.hirello.domain.user.entity.User;

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
    private LocalDateTime deadlineAt;

    @Column
    private String worker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id")
    private Columns columns;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "card")
    private List<Comment> commentList;
}
