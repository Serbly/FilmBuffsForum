package org.example.filmbuffsforum.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.dto.CreateUserByAdminRequest;
import org.example.filmbuffsforum.auth.dto.UpdateUserRequest;
import org.example.filmbuffsforum.auth.exception.AlreadyExitsException;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void createUserByAdmin(CreateUserByAdminRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExitsException("User already exists");
        }

        User user = User.builder()
                .roles(request.getRoles())
                .password(request.getPassword())
                .isDeleted(false)
                .username(request.getUsername())
                .build();

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeleted(true);
        userRepository.save(user);
    }

    public void restoreUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeleted(false);
        userRepository.save(user);
    }

    public void updateUser(Integer id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRoles(request.getRoles());
        userRepository.save(user);
    }
}
