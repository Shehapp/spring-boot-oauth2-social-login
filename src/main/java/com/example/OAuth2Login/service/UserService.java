package com.example.OAuth2Login.service;

import com.example.OAuth2Login.config.jwt.JwtService;
import com.example.OAuth2Login.dto.RequestLogin;
import com.example.OAuth2Login.dto.RequestRegister;
import com.example.OAuth2Login.entity.AuthProvider;
import com.example.OAuth2Login.entity.Role;
import com.example.OAuth2Login.entity.User;
import com.example.OAuth2Login.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    public String authenticate(RequestLogin requestLogin) {
        System.out.println(requestLogin);
        Authentication authentication;
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestLogin.getEmail(),
                            requestLogin.getPassword()
                    )
            );

        return jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                .withUsername(requestLogin.getEmail())
                .password("")
                .authorities(authentication.getAuthorities())
                .build()
        );
    }

    public void save(RequestRegister requestLogin) {
        User user=User
                .builder()
                .name(requestLogin.getUsername())
                .email(requestLogin.getEmail())
                .password(passwordEncoder.encode(requestLogin.getPassword()))
                .role(Role.USER)
                .authProvider(AuthProvider.LOCAL)
                .build();
        System.out.println(user);
        userRepository.save(user);
    }
}
