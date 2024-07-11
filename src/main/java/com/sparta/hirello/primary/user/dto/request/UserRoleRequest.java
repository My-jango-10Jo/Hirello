package com.sparta.hirello.primary.user.dto.request;

import com.sparta.hirello.primary.user.entity.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRoleRequest {

    @NotNull
    private UserRole role;

}
