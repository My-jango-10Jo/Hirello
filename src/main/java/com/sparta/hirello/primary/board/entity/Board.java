package com.sparta.hirello.primary.board.entity;

import com.sparta.hirello.primary.board.dto.request.BoardRequest;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.user.entity.User;
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
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String description;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardMember> boardMembers = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Progress> progresses = new ArrayList<>();

    /**
     * 생성자
     */
    private Board(BoardRequest request, User user) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        addMember(user, BoardRole.MANAGER); // 보드 생성자는 자동으로 매니저 권한을 갖는다.
    }

    public static Board of(BoardRequest request, User user) {
        return new Board(request, user);
    }

    public void addMember(User user, BoardRole role) {
        boardMembers.add(BoardMember.of(user, this, role));
    }

    public void update(BoardRequest request) {
        this.title = request.getTitle();
        this.description = request.getDescription();
    }

}
