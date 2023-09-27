package com.example.OAuth2Login.config;

import com.example.OAuth2Login.entity.User;
import com.example.OAuth2Login.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@AllArgsConstructor
public class Config {

    private final UserRepository userRepository;


    @Bean
    public UserDetailsService userDetailsService(){
        return (email)->{

                Optional<User> optionalUser=userRepository.findByEmail(email);

                if(optionalUser.isPresent()){
                    User user=optionalUser.get();

                    if(!user.getAuthProvider().toString().equalsIgnoreCase("local")){
                        throw new RuntimeException("you are already signed up with "+user.getAuthProvider().toString()+" account");
                    }

                    return org.springframework.security.core.userdetails.User
                            .withUsername(email)
                            .password(user.getPassword())
                            .authorities(user.getRole().toString())
                            .build();

                }else {
                    throw new RuntimeException("email not found");
                }

        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
