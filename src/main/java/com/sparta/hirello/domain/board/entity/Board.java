package com.sparta.hirello.domain.board.entity;

import com.sparta.hirello.domain.column.entity.Columns;
import jakarta.persistence.*;
import lombok.Getter;
import com.sparta.hirello.domain.user.entity.User;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String boardName;

    private String headline;

    @OneToMany(mappedBy = "board")
    private List<Columns> columnsList;
}
