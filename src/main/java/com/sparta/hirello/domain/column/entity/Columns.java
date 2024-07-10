package com.sparta.hirello.domain.column.entity;

import com.sparta.hirello.domain.card.entity.Card;
import jakarta.persistence.*;
import lombok.Getter;

import com.sparta.hirello.domain.user.entity.User;
import com.sparta.hirello.domain.board.entity.Board;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "columns")
public class Columns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long columnId;

    @Column(nullable=false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "columns")
    private List<Card> cardList;
}
