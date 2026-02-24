    package com.example.demo.utils;

    import com.example.demo.principal.CustomOAuth2User;
    import com.example.demo.security.JwtUtil;
    import jakarta.servlet.ServletException;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.AllArgsConstructor;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.ResponseCookie;
    import org.springframework.security.web.authentication.AuthenticationFailureHandler;
    import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
    import org.springframework.stereotype.Component;

    import java.io.IOException;

    @Component
    @AllArgsConstructor
    public class CustomAuthenticationHandler implements AuthenticationSuccessHandler {

        private final JwtUtil jwtUtils;
        private SessionStore sessionStore;
        private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationHandler.class);


        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response,
                                            org.springframework.security.core.Authentication authentication
        ) throws IOException, ServletException {

            logger.info("UI login SUCCESS for user: {}", authentication.getName());

            String sessionId = request.getSession().getId();
            String email = authentication.getName();

            sessionStore.addSession(sessionId, email);

            CustomOAuth2User user = (CustomOAuth2User) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(user.getEmail());

            ResponseCookie cookie = ResponseCookie.from("access_token", jwt)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(60 * 60)
                    .sameSite("Lax")
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            response.sendRedirect("/profile");
        }

        public AuthenticationFailureHandler failureHandler() {
            return (request, response, exception) -> {
                String email = request.getParameter("email");
                logger.warn("UI login FAILED for user: {}. Reason: {}", email, exception.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
            };
        }
    }
