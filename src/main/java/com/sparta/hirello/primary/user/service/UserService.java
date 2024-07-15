package com.sparta.hirello.primary.user.service;

import com.sparta.hirello.primary.user.dto.request.PasswordRequest;
import com.sparta.hirello.primary.user.dto.request.ProfileRequest;
import com.sparta.hirello.primary.user.dto.request.SignupRequest;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.entity.UserRole;
import com.sparta.hirello.primary.user.repository.RefreshTokenRepository;
import com.sparta.hirello.primary.user.repository.UserRepository;
import com.sparta.hirello.secondary.exception.PasswordNotMatchException;
import com.sparta.hirello.secondary.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    @Value("${admin.token}")
    private String adminToken;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository; // 추후 Redis 사용

    /**
     * 회원가입
     */
    @Transactional
    public User signup(SignupRequest request) {
        // username 중복 확인
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        // userRole 확인
        UserRole role = UserRole.USER;
        if (request.isAdmin()) {
            if (!request.getAdminToken().equals(adminToken)) {
                throw new IllegalArgumentException("어드민 토큰이 일치하지 않습니다.");
            }
            role = UserRole.ADMIN;
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());


        return userRepository.save(User.of(request, encodedPassword, role));
    }

    /**
     * 로그아웃
     */
    @Transactional
    public void logout(User user) {
        deleteRefreshToken(user);
    }

    /**
     * 전체 회원 조회 (admin only)
     */
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * 회원 조회
     */
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    /**
     * 프로필 수정
     */
    @Transactional
    public User updateProfile(Long userId, ProfileRequest request, User requestUser) {
        User user = getUserWithVerification(userId, requestUser);
        user.updateProfile(request);
        return user;
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public User updatePassword(Long userId, PasswordRequest request, User requestUser) {
        User user = getUserWithVerification(userId, requestUser);
        // 현재 비밀번호 일치 여부 확인
        String currentPassword = user.getPassword();
        if (isNotSamePassword(request.getCurrentPassword(), currentPassword)) {
            throw new PasswordNotMatchException("현재 비밀번호가 일치하지 않습니다.");
        }
        // 현재 비밀번호와 새 비밀번호 일치 여부 확인
        String newPassword = request.getNewPassword();
        if (isNotSamePassword(newPassword, currentPassword)) {
            throw new PasswordNotMatchException("현재 비밀번호와 동일한 비밀번호로 수정할 수 없습니다.");
        }
        // 새 비밀번호 일치 여부 확인
        String retypedNewPassword = request.getRetypedNewPassword();
        if (!newPassword.equals(retypedNewPassword)) {
            throw new PasswordNotMatchException("새 비밀번호가 일치하지 않습니다.");
        }
        user.updatePassword(passwordEncoder.encode(newPassword));
        return user;
    }

    /**
     * 회원 권한 변경 (admin only)
     */
    @Transactional
    public User updateUserRole(Long userId, UserRole role) {
        User user = getUser(userId);
        // 같은 권한으로 변경하는지 확인
        if (user.getRole().equals(role)) {
            throw new IllegalArgumentException("해당 회원의 권한이 이미 " + role + " 상태입니다.");
        }
        user.updateRole(role);
        return user;
    }

    private User getUserWithVerification(Long userId, User requestUser) {
        requestUser.verifyAccessAuthority(userId);
        return getUser(userId);
    }

    private boolean isNotSamePassword(String rawPassword, String encodedPassword) {
        return !passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private void deleteRefreshToken(User user) {
        refreshTokenRepository.deleteByUsername(user.getUsername());
    }

}
