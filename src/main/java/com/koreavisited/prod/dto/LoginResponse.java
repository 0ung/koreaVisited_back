package com.koreavisited.prod.dto;

import com.koreavisited.prod.constants.Role;
import com.koreavisited.prod.entity.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String email;
    private String nickname;
    private String preferredLanguage;
    private Role role;


    public static LoginResponse fromUser(User user) {
        return LoginResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .preferredLanguage(user.getPreferredLanguage())
                .role(user.getRole())
                .build();
    }
}
