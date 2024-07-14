package com.sparta.hirello.primary.board.entity;

import com.sparta.hirello.primary.board.dto.request.BoardRequest;
import com.sparta.hirello.primary.progress.entity.Progress;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.secondary.base.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
<<<<<<< Updated upstream

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
=======
>>>>>>> Stashed changes

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 보드 생성자

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Progress> progressList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardMember> boardMembers = new ArrayList<>();

    /**
     * 생성자
     */
    private Board(BoardRequest request, User user) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.user = user;
        addMember(user, BoardRole.MANAGER); // 보드 생성자는 자동으로 매니저 권한을 갖는다.
    }

    public static Board of(BoardRequest request, User user) {
        return new Board(request, user);
    }

    public void update(BoardRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
    }

    public void addMember(User user, BoardRole role) {
        boardMembers.add(BoardMember.of(user, this, role));
    }
<<<<<<< Updated upstream

    public void checkColumn(Long columnId) {
        boolean columnExist = this.progressList.stream()
                .anyMatch(column -> column.getId().equals(columnId));

        if (!columnExist) {
            throw new EntityNotFoundException("컬럼이 존재하지 않습니다.");
        }
    }

=======
>>>>>>> Stashed changes
}
