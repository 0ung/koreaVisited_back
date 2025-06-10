package com.koreavisited.prod.service;

import com.koreavisited.prod.config.CryptoConfig;
import com.koreavisited.prod.constants.Role;
import com.koreavisited.prod.dto.LoginRequest;
import com.koreavisited.prod.dto.LoginResponse;
import com.koreavisited.prod.dto.SignupRequest;
import com.koreavisited.prod.entity.User;
import com.koreavisited.prod.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CryptoConfig.CryptoService cryptoService;

    /**
     * 회원가입
     */
    public void signup(SignupRequest req) {

        // 1) 비밀번호 & 확인 일치
        if (!req.password().equals(req.confirmPassword()))
            throw new IllegalArgumentException("password mismatch");

        // 2) 중복 이메일
        if (userRepository.existsByEmail((req.email())))
            throw new IllegalStateException("email already exists");

        // 3) 저장 엔티티 생성
        User u = User.builder()
                .email(cryptoService.encode(req.email()))         // 민감정보 암호화
                .password(passwordEncoder.encode(req.password()))  // BCrypt
                .nickname(req.nickname())
                .preferredLanguage(req.preferredLanguage())
                .role(Role.USER)
                .build();

        userRepository.save(u);
    }

    //로그인
    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Remember Me 값: " + loginRequest.isRememberMe()); // 🔥 핵심!

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(auth);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
                    , SecurityContextHolder.getContext());

            return LoginResponse.fromUser((User) auth.getPrincipal());
        } catch (BadCredentialsException e) {
            return null;
        }
    }


    public void logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
    }
}
