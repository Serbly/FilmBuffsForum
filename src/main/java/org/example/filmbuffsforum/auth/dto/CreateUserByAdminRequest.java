package org.example.filmbuffsforum.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.filmbuffsforum.auth.model.RoleType;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserByAdminRequest {
    private String username;

    private Set<RoleType> roles;

    private String password;
}
