package com.sparta.hirello.primary.board.entity;

import com.sparta.hirello.primary.column.entity.Columns;
import com.sparta.hirello.primary.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
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
