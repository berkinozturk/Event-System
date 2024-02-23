package com.berkinozturk.event.request;

import com.berkinozturk.event.entity.RoleType;
import lombok.*;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    // You can add validation to these.
    private String username;
    private String password;
    private String email;
    private String fullName;
    private RoleType role;

}
