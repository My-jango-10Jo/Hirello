package com.sparta.hirello.primary.board.entity;

import com.sparta.hirello.primary.board.dto.request.BoardRequest;
import com.sparta.hirello.primary.column.entity.Columns;
import com.sparta.hirello.primary.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private List<Columns> columnsList = new ArrayList<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<BoardMember> memberList = new ArrayList<>();

    private Board(BoardRequest requestDto, User user) {
        this.user = user;
        this.boardName = requestDto.getBoardName();
        this.headline = requestDto.getHeadline();
    }

    public static Board of(BoardRequest requestDto, User user) {
        return new Board(requestDto, user);
    }

    public void update(String boardName, String headline) {
        this.boardName = boardName;
        this.headline = headline;
    }
}
