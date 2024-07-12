package com.sparta.hirello.primary.board.entity;

import com.sparta.hirello.primary.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Enumerated(value = EnumType.STRING)
    private BoardAuthority boardAuthority; // [USER, MANAGER]

    private BoardMember(User user, Board board) {
        this.user = user;
        this.board = board;
        this.boardAuthority = BoardAuthority.USER;
    }

    private BoardMember(User user, Board board, BoardAuthority boardAuthority) {
        this.user = user;
        this.board = board;
        this.boardAuthority = boardAuthority;
    }

    public static BoardMember of(User user, Board board) {
        return new BoardMember(user, board);
    }

    public static BoardMember of(User user, Board board, BoardAuthority boardAuthority) {
        return new BoardMember(user, board, boardAuthority);
    }

    public void updateRole(BoardAuthority boardAuthority) {
        this.boardAuthority = boardAuthority;
    }
}
