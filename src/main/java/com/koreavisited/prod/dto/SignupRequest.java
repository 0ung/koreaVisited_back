package com.koreavisited.prod.dto;

public record SignupRequest(

        String email,

        String password,

        String confirmPassword,

        String nickname,

        String preferredLanguage    /* ko | en | ja */
) {}
