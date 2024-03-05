package com.berkinozturk.event.response;

import com.berkinozturk.event.entity.RoleType;
import lombok.*;

@Data
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class CreateUserResponse {
    private String id;
    private String username;
    private String email;
    private String fullName;
    private RoleType role;
}
