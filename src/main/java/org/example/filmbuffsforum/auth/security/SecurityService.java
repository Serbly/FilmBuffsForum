package org.example.filmbuffsforum.auth.security;

import lombok.RequiredArgsConstructor;
import org.example.filmbuffsforum.auth.dto.AuthResponse;
import org.example.filmbuffsforum.auth.dto.CreateUserRequest;
import org.example.filmbuffsforum.auth.dto.RefreshTokenRequest;
import org.example.filmbuffsforum.auth.dto.RefreshTokenResponse;
import org.example.filmbuffsforum.auth.exception.AlreadyExitsException;
import org.example.filmbuffsforum.auth.exception.RefreshTokenException;
import org.example.filmbuffsforum.auth.redis.RefreshToken;
import org.example.filmbuffsforum.auth.model.RoleType;
import org.example.filmbuffsforum.auth.model.User;
import org.example.filmbuffsforum.auth.repository.UserRepository;
import org.example.filmbuffsforum.auth.security.jwt.JwtUtils;
import org.example.filmbuffsforum.auth.service.RefreshTokenService;
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

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticateUser(CreateUserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).orElseThrow().isDeleted()) {
            throw new RuntimeException("User not found");
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return AuthResponse.builder()
                .id(userDetails.getId())
                .token(jwtUtils.generateToken(userDetails))
                .refreshToken(refreshToken.getToken())
                .username(userDetails.getUsername())
                .roles(roles)
                .isDeleted(userDetails.isDeleted())
                .build();
    }

    public void createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AlreadyExitsException("User already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .isDeleted(false)
                .build();
        user.setRoles(Collections.singleton(RoleType.ROLE_USER));

        userRepository.save(user);
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        return refreshTokenService.findByRefreshToken(refreshToken)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getId)
                .map(userId -> {
                    User tokenOwner = userRepository.findById(userId).orElseThrow(() ->
                            new RefreshTokenException("Exception trying to get token for userId " + userId));
                    String token = jwtUtils.generateTokenFromUsername(tokenOwner.getUsername());

                    return new RefreshTokenResponse(token, refreshTokenService.createRefreshToken(userId).getToken());
                }).orElseThrow(() -> new RefreshTokenException(refreshToken, "Refresh token not found"));
    }
}
