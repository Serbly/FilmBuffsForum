package org.example.filmbuffsforum.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.dto.CreateUserRequest;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.auth.repository.UserRepository;
import org.example.filmbuffsforum.auth.security.AppUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User getUserInfo() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            Integer userId = userDetails.getId();
            return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        }
        return null;
    }

    public void updateUser(CreateUserRequest request) {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            Integer userId = userDetails.getId();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            userRepository.save(user);
        }
    }
}
