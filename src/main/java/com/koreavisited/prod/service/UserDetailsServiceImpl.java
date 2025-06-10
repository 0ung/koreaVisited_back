package com.koreavisited.prod.service;

import com.koreavisited.prod.config.CryptoConfig;
import com.koreavisited.prod.constants.Role;
import com.koreavisited.prod.dto.LoginRequest;
import com.koreavisited.prod.dto.SignupRequest;
import com.koreavisited.prod.entity.User;
import com.koreavisited.prod.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final CryptoConfig.CryptoService cryptoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String encryptedEmail = cryptoService.encode(username);
        User user = userRepository.findByEmail(encryptedEmail);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        user.setEmail(cryptoService.decode(user.getEmail()));
        return user;
    }
}
