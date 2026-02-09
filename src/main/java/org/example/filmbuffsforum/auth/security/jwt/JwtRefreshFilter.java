package org.example.filmbuffsforum.auth.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRefreshFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtService.resolveAccessToken(request);

        if (accessToken == null || jwtService.isExpired(accessToken)) {
            String refreshToken = jwtService.resolveRefreshToken(request);

            if (refreshToken != null && jwtService.isValid(refreshToken)) {
                String newAccess = jwtService.generateAccessToken(
                        jwtService.extractUsername(refreshToken)
                );

                ResponseCookie cookie = ResponseCookie.from("JWT", newAccess)
                        .httpOnly(true)
                        .path("/")
                        .maxAge(900)
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            }
        }

        filterChain.doFilter(request, response);
    }
}
