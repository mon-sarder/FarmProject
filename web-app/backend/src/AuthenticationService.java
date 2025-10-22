package com.example.queueinv;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles user storage and registration logic.
 * Implements UserDetailsService to plug into Spring Security.
 */
@Service
public class AuthService implements UserDetailsService {

    // In-memory "database" for users. A real app would use a SQL/NoSQL database.
    private final Map<String, UserDetails> userStore = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder;

    public AuthService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

        // For testing: create a default admin user
        // In a real app, you would remove this.
        if (userStore.isEmpty()) {
            String adminUsername = "admin";
            String adminPassword = passwordEncoder.encode("Password123!"); // Matches validation rules
            UserDetails adminUser = new User(adminUsername, adminPassword, Collections.emptyList());
            userStore.put(adminUsername.toLowerCase(), adminUser);
        }
    }

    /**
     * This is the method Spring Security calls when it needs to authenticate a user.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userStore.get(username.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }

    /**
     * Handles new user registration.
     */
    public void register(RegisterDTO registerDTO) {
        String username = registerDTO.username();
        String password = registerDTO.password();

        // 1. Check for unique username
        if (userStore.containsKey(username.toLowerCase())) {
            throw new IllegalStateException("Username already taken. Please choose another.");
        }

        // 2. Hash the password
        String hashedPassword = passwordEncoder.encode(password);

        // 3. Create and save the new user
        UserDetails newUser = new User(username, hashedPassword, Collections.emptyList());
        userStore.put(username.toLowerCase(), newUser);
    }
}
