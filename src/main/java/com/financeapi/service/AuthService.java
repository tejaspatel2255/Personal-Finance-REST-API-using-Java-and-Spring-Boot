package com.financeapi.service;

import com.financeapi.dto.AuthRequest;
import com.financeapi.dto.AuthResponse;
import com.financeapi.dto.LoginRequest;
import com.financeapi.model.User;
import com.financeapi.repository.UserRepository;
import com.financeapi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final com.financeapi.repository.CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        user = userRepository.save(user);
        seedDefaultCategories(user);
        return new AuthResponse(jwtService.generateToken(user.getId(), user.getUsername()));
    }

    private void seedDefaultCategories(User user) {
        java.util.List<CategorySeed> defaults = java.util.List.of(
            new CategorySeed("Salary", com.financeapi.model.CategoryType.INCOME),
            new CategorySeed("Freelance / Business", com.financeapi.model.CategoryType.INCOME),
            new CategorySeed("Investments", com.financeapi.model.CategoryType.INCOME),
            new CategorySeed("Groceries & Food", com.financeapi.model.CategoryType.EXPENSE),
            new CategorySeed("Rent & Housing", com.financeapi.model.CategoryType.EXPENSE),
            new CategorySeed("Utilities & Bills", com.financeapi.model.CategoryType.EXPENSE),
            new CategorySeed("Transportation & Fuel", com.financeapi.model.CategoryType.EXPENSE),
            new CategorySeed("Dining Out & Entertainment", com.financeapi.model.CategoryType.EXPENSE),
            new CategorySeed("Shopping & Lifestyle", com.financeapi.model.CategoryType.EXPENSE),
            new CategorySeed("Healthcare & Medical", com.financeapi.model.CategoryType.EXPENSE),
            new CategorySeed("Education & Learning", com.financeapi.model.CategoryType.EXPENSE)
        );

        for (CategorySeed seed : defaults) {
            categoryRepository.save(
                com.financeapi.model.Category.builder()
                    .name(seed.name())
                    .type(seed.type())
                    .user(user)
                    .build()
            );
        }
    }

    private record CategorySeed(String name, com.financeapi.model.CategoryType type) {}

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        return new AuthResponse(jwtService.generateToken(user.getId(), user.getUsername()));
    }
}
