package com.sparta.hirello.primary.user.dto.response;

import com.sparta.hirello.primary.user.entity.User;
import com.sparta.hirello.primary.user.entity.UserRole;
import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String username;
    private String name;
    private UserRole role;

    private UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.role = user.getRole();
    }

    public static UserResponse of(User user) {
        return new UserResponse(user);
    }

}