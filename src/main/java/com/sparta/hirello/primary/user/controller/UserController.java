package com.sparta.hirello.primary.user.controller;

import com.sparta.hirello.primary.user.dto.request.PasswordRequest;
import com.sparta.hirello.primary.user.dto.request.ProfileRequest;
import com.sparta.hirello.primary.user.dto.request.SignupRequest;
import com.sparta.hirello.primary.user.dto.request.UserRoleRequest;
import com.sparta.hirello.primary.user.dto.response.UserResponse;
import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.service.UserService;
import com.sparta.hirello.secondary.base.dto.CommonResponse;
import com.sparta.hirello.secondary.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.sparta.hirello.secondary.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/user/signup")
    public ResponseEntity<CommonResponse<?>> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        User user = userService.signup(request);

        return getResponseEntity(UserResponse.of(user), "회원가입 성공");
    }

    /**
     * 로그아웃 - 내부적으로 상태가 변하므로 POST 선택
     */
    @PostMapping("/user/logout")
    public ResponseEntity<CommonResponse<?>> logout(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long response = userService.logout(userDetails.getUser());

        return getResponseEntity(response, "로그아웃 성공");
    }

    /**
     * 전체 회원 조회 (admin only)
     */
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users")
    public ResponseEntity<CommonResponse<?>> getAllUsers(
            @PageableDefault(
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable
    ) {
        Page<User> page = userService.getAllUsers(pageable);
        Page<UserResponse> response = page.map(UserResponse::of);

        return getResponseEntity(response, "전체 회원 조회 성공");
    }

    /**
     * 회원 프로필 조회
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<CommonResponse<?>> getUser(
            @PathVariable Long userId
    ) {
        User user = userService.getUser(userId);

        return getResponseEntity(UserResponse.of(user), "프로필 조회 성공");
    }

    /**
     * 회원 프로필 수정
     */
    @PatchMapping("/users/{userId}")
    public ResponseEntity<CommonResponse<?>> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody ProfileRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userService.updateProfile(userId, request, userDetails.getUser());

        return getResponseEntity(UserResponse.of(user), "프로필 수정 성공");
    }

    /**
     * 회원 비밀번호 수정
     */
    @PatchMapping("/users/{userId}/password")
    public ResponseEntity<CommonResponse<?>> updatePassword(
            @PathVariable Long userId,
            @Valid @RequestBody PasswordRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userService.updatePassword(userId, request, userDetails.getUser());

        return getResponseEntity(UserResponse.of(user), "비빌번호 변경 성공");
    }

    /**
     * 회원 권한 변경 (admin only)
     */
    @PreAuthorize("isAuthenticated() and hasRole('ROLE_ADMIN')")
    @PatchMapping("/admin/users/{userId}/role")
    public ResponseEntity<CommonResponse<?>> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UserRoleRequest request
    ) {
        User user = userService.updateUserRole(userId, request.getRole());

        return getResponseEntity(UserResponse.of(user), "회원 권한 변경 성공");
    }

}
