package com.example.diplomneto.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequest {
    @JsonProperty("email")
    private String login;

    @JsonProperty("password")
    private String password;
}