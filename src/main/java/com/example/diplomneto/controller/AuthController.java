package com.example.diplomneto.controller;

import com.example.diplomneto.dto.LoginRequest;
import com.example.diplomneto.dto.LoginResponse;
import com.example.diplomneto.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader(value = "auth-token", required = false) String authToken) {
        if (authToken != null) {
            authService.logout(authToken);
        }
        return ResponseEntity.ok().build();
    }


}