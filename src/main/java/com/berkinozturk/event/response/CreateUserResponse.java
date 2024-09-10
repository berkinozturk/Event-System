package com.berkinozturk.event.response;

import com.berkinozturk.event.entity.RoleType;
import lombok.*;

@Data
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class CreateUserResponse {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private RoleType role;
}
