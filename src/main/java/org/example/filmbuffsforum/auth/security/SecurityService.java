package org.example.filmbuffsforum.auth.security;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.dto.*;
import org.example.filmbuffsforum.auth.exception.AlreadyExitsException;
import org.example.filmbuffsforum.auth.model.RoleType;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.auth.repository.UserRepository;
import org.example.filmbuffsforum.auth.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticateUser(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isDeleted()) {
            throw new RuntimeException("User deleted");
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            ));
        } catch (Exception e) {
            throw new RuntimeException("Bad credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        long refreshTtl = request.isRememberMe()
                ? 7 * 24 * 60 * 60  // 7 дней
                : 24 * 60 * 60;    // 1 день

        String accessToken = jwtService.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtService.generateRefreshToken(userDetails.getUsername());

        return AuthResponse.builder()
                .id(userDetails.getId())
                .token(accessToken)
                .refreshToken(refreshToken)
                .username(userDetails.getUsername())
                .roles(roles)
                .isDeleted(userDetails.isDeleted())
                .refreshTtl(refreshTtl)
                .build();
    }

    public void createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExitsException("User already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .isDeleted(false)
                .build();
        user.setRoles(Collections.singleton(RoleType.ROLE_USER));

        userRepository.save(user);
    }

//    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
//        String refreshToken = request.getRefreshToken();
//
//        return refreshTokenService.findByRefreshToken(refreshToken)
//                .map(refreshTokenService::checkRefreshToken)
//                .map(RefreshToken::getId)
//                .map(userId -> {
//                    User tokenOwner = userRepository.findById(userId).orElseThrow(() ->
//                            new RefreshTokenException("Exception trying to get token for userId " + userId));
//                    String token = jwtService.generateAccessToken(tokenOwner.getUsername());
//
//                    return new RefreshTokenResponse(token, refreshTokenService.createRefreshToken(userId).getToken());
//                }).orElseThrow(() -> new RefreshTokenException(refreshToken, "Refresh token not found"));
//    }
}
