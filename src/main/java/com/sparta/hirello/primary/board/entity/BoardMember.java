package com.sparta.hirello.primary.board.entity;

import com.sparta.hirello.primary.user.entity.User;
import jakarta.persistence.*;
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
    private BoardRole boardRole; // [USER, MANAGER]

    private BoardMember(User user, Board board, BoardRole role) {
        this.user = user;
        this.board = board;
        this.boardRole = role;
    }

    public static BoardMember of(User user, Board board, BoardRole role) {
        return new BoardMember(user, board, role);
    }

    public void updateRole(BoardRole boardRole) {
        this.boardRole = boardRole;
    }

}
