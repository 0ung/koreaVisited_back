package com.koreavisited.prod.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class LoginRequest {
    private String email;
    private String password;
    @JsonProperty("remember-me")
    private boolean rememberMe;
}
