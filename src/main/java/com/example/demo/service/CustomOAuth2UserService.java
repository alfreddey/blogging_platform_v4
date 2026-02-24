package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.principal.CustomOAuth2User;
import com.example.demo.repository.interfaces.UserRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;

@Service
public class CustomOAuth2UserService extends OidcUserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest)
            throws OAuth2AuthenticationException {

        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        String name = oidcUser.getAttribute("name");

        if (email == null || name == null) {
            throw new OAuth2AuthenticationException("Required attributes missing");
        }

        User user = userRepository.findByEmail(email)
                .map(existing -> {
                    existing.setName(name);
                    return userRepository.save(existing);
                })
                .orElseGet(() -> createNewUser(email, name));

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of("ROLE_USER"));
            userRepository.save(user);
        }

        return new CustomOAuth2User(oidcUser, user);
    }

    private User createNewUser(String email, String name) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        return userRepository.save(newUser);
    }
}