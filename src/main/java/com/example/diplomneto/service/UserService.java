package com.example.diplomneto.service;

import com.example.diplomneto.exception.UnauthorizedException;
import com.example.diplomneto.model.User;
import com.example.diplomneto.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByToken(String authToken) {
        if (authToken == null || authToken.isEmpty()) {
            throw new UnauthorizedException("Auth token is missing");
        }
        String token = authToken.startsWith("Bearer ")
                ? authToken.substring(7)
                : authToken;

        return userRepository.findByAuthToken(token)
                .orElseThrow(() -> new UnauthorizedException("Invalid auth token"));
    }
}
