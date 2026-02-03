package org.example.filmbuffsforum.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.filmbuffsforum.auth.model.RoleType;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String username;

    private String password;

    private Set<RoleType> roles;
}