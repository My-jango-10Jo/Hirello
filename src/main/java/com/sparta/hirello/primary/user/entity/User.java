package com.sparta.hirello.primary.user.entity;

import com.sparta.hirello.primary.user.dto.request.ProfileRequest;
import com.sparta.hirello.primary.user.dto.request.SignupRequest;
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
@Table(name = "users")
public class User extends Timestamped {

    private static final int PASSWORD_HISTORY_LIMIT = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String username; // 사용자 아이디

    private String password;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(joinColumns = @JoinColumn(name = "user_id"))
    private List<String> passwordHistory = new ArrayList<>(); // 최근 변경한 비밀번호

    /**
     * 생성자
     */
    private User(SignupRequest request, String encodedPassword, UserRole role) {
        this.username = request.getUsername();
        this.password = encodedPassword;
        this.name = request.getName();
        this.role = role;
        addPasswordToHistory(encodedPassword);
    }

    public static User of(SignupRequest request, String encodedPassword, UserRole role) {
        return new User(request, encodedPassword, role);
    }

    /**
     * 회원 프로필 수정
     */
    public void updateProfile(ProfileRequest request) {
        this.name = request.getName();
    }

    /**
     * 회원 비밀번호 변경
     */
    public void updatePassword(String newPassword) {
        if (isPasswordInHistory(newPassword)) {
            throw new IllegalArgumentException("최근에 사용한 비밀번호로는 변경할 수 없습니다.");
        }
        this.password = newPassword;
        addPasswordToHistory(newPassword);
    }

    /**
     * 회원 권한 변경
     */
    public void updateRole(UserRole role) {
        this.role = role;
    }

    /**
     * 해당 userId에 대한 액세스 권한 검증
     */
    public void verifyAccessAuthority(Long userId) {
        if (!this.id.equals(userId) && !this.role.equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("액세스 권한이 없는 회원입니다.");
        }
    }

    /**
     * 최근에 변경한 비밀번호인지 확인
     */
    private boolean isPasswordInHistory(String password) {
        return passwordHistory.contains(password);
    }

    /**
     * 새로운 비밀번호 히스토리에 저장
     */
    private void addPasswordToHistory(String newPassword) {
        if (passwordHistory.size() == PASSWORD_HISTORY_LIMIT) { // 히스토리가 가득 찬 경우
            passwordHistory.remove(0); // 가장 오래된 비밀번호 제거
        }
        passwordHistory.add(newPassword);
    }

}