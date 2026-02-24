package com.example.demo.security;

import com.example.demo.service.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private TokenBlacklistService blacklistService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return true;
        }

        String path = request.getServletPath();

        return !path.startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws java.io.IOException, ServletException {

        var authHeader = request.getHeader("Authorization");

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                var token = authHeader.substring(7);

                if (jwtUtil.validateToken(token)) {

                    if (blacklistService.isBlacklisted(token)) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }

                    var email = jwtUtil.getUserFromToken(token);
                    var role = jwtUtil.getRoleFromToken(token);
                    var authorities = List.of(new SimpleGrantedAuthority(role));

                    var authenticationToken = new UsernamePasswordAuthenticationToken(
                            email, null, authorities);

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    log.info("API JWT authentication SUCCESS for user: {}", email);
                }
            }
        } catch (Exception e) {
            log.warn("API JWT authentication FAILED: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
