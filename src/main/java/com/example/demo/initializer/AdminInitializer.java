package com.example.demo.initializer;

import com.example.demo.entity.User;
import com.example.demo.enums.UserRole;
import com.example.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("firstadmin@yourcompany.com").isEmpty()) {
            User admin = new User();

            admin.setEmail("firstadmin@yourcompany.com");
            admin.setName("System Admin");

            if (admin.getRoles() == null) {
                admin.setRoles(List.of(UserRole.ROLE_USER, UserRole.ROLE_ADMIN));
            }

            userRepository.save(admin);
        }
    }
}
