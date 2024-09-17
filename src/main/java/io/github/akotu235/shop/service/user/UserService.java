package io.github.akotu235.shop.service.user;


import io.github.akotu235.shop.result.Result;
import io.github.akotu235.shop.service.user.projection.UserWriteModel;
import io.github.akotu235.shop.service.user.model.User;
import io.github.akotu235.shop.service.user.repository.UserRepository;
import io.github.akotu235.shop.service.user.service.EmailCheckerService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmailCheckerService emailCheckerService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, EmailCheckerService emailCheckerService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailCheckerService = emailCheckerService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Result<User> createUser(UserWriteModel newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User user = userRepository.save(new User(newUser));
        emailCheckerService.sendVerificationEmail(user);
        return new Result<>(true, user, "success.user-creation", user.toString(), user.getEmail());
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUser(Authentication authentication) {
        return userRepository.findByUsername(authentication.getName());
    }

    public void afterUserLogin(Authentication authentication) {
        User user = getUser(authentication);
        user.setLastLoginDate(LocalDateTime.now());
        updateUser(user);
    }

    public static boolean hasRole(String role, Authentication authentication) {
        return authentication
                .getAuthorities()
                .stream()
                .anyMatch(authority -> ("ROLE_" + role).equals(authority.getAuthority()));
    }
}
