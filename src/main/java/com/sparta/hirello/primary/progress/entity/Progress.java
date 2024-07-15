package com.sparta.hirello.primary.progress.entity;

import com.sparta.hirello.primary.board.entity.Board;
import com.sparta.hirello.primary.card.entity.Card;
import com.sparta.hirello.secondary.base.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Progress extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Long id;

    private String title;

    @Column(name = "progress_order")
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "progress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    /**
     * 생성자
     */
    private Progress(String title, Board board) {
        this.title = title;
        this.board = board;
        this.order = board.getProgresses().size(); // 새로 생성한 프로그레스의 순서는 보드 맨 뒤로 지정
        board.getProgresses().add(this);
    }

    public static Progress of(String name, Board board) {
        return new Progress(name, board);
    }

    public void updateOrder(int order) {
        this.order = order;
    }

}
