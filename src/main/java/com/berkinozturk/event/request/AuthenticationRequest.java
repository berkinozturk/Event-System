package com.berkinozturk.event.request;

import lombok.*;

@Setter
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
    // You can add validation to these.
    private String email;
    String password;
}
