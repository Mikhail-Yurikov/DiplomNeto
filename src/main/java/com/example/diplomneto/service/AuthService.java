package com.example.diplomneto.service;


import com.example.diplomneto.dto.LoginRequest;
import com.example.diplomneto.dto.LoginResponse;
import com.example.diplomneto.exception.UnauthorizedException;
import com.example.diplomneto.model.User;
import com.example.diplomneto.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByLogin(request.getLogin()).orElse(null);

        // Проверяем, существует ли пользователь и совпадает ли пароль
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(
                    List.of("Неверный логин или пароль"),
                    List.of()
            );
        }

        String token = UUID.randomUUID().toString();
        user.setAuthToken(token);
        userRepository.save(user);

        return new LoginResponse(token);
    }

    public void logout(String authToken) {
        String token = authToken.startsWith("Bearer ")
                ? authToken.substring(7)
                : authToken;

        User user = userRepository.findByAuthToken(token).orElse(null);
        if (user != null) {
            user.setAuthToken(null);
            userRepository.save(user);
        }
    }
}