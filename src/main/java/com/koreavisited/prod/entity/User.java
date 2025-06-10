package com.koreavisited.prod.entity;


import com.koreavisited.prod.constants.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_email", columnNames = "email"),
        indexes = {
                @Index(name = "idx_user_email", columnList = "email")
        }
)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 로그인 ID
    @Column(nullable = false, length = 120)
    @Setter
    private String email;

    // BCrypt 해시
    @Column(nullable = false, length = 60)
    private String password;

    @Column(nullable = false, length = 40)
    private String nickname;

    @Column(nullable = false, length = 5)
    private String preferredLanguage;

    // ✔ FK 없이 단일 열로 저장
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }


    //TODO 계정 만료
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    //TODO 계정 잠금
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    //TODO 계정 비밀번호 만료
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    //TODO 계정 활성화 처리
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}