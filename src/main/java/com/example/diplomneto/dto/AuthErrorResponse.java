package com.example.diplomneto.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthErrorResponse {
    @JsonProperty("email")
    private List<String> email;

    @JsonProperty("password")
    private List<String> password;
}