package com.example.diplomneto.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Getter
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    private final List<String> emailErrors;
    private final List<String> passwordErrors;

    public UnauthorizedException(String message) {
        super(message);
        this.emailErrors = List.of();
        this.passwordErrors = List.of();
    }

    public UnauthorizedException(List<String> emailErrors, List<String> passwordErrors) {
        super("Invalid login or password");
        this.emailErrors = emailErrors != null ? emailErrors : List.of();
        this.passwordErrors = passwordErrors != null ? passwordErrors : List.of();
    }
}