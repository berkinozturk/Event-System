package com.berkinozturk.event.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@AllArgsConstructor
@Setter
@Data
@Getter
@Document
@Builder
public class UserEntity implements Serializable{

    @Id
    private String id;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private RoleType role;

}
