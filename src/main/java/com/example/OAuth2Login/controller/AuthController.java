package com.example.OAuth2Login.controller;


import com.example.OAuth2Login.dto.RequestLogin;
import com.example.OAuth2Login.dto.RequestRegister;
import com.example.OAuth2Login.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;


    @PostMapping("/login")
    ResponseEntity<?> f2(@RequestBody RequestLogin requestLogin) {
            return ResponseEntity.ok(userService.authenticate(requestLogin));

    }


    @PostMapping("/register")
    ResponseEntity<?> f3(@RequestBody RequestRegister requestLogin) {
        userService.save(requestLogin);
        return ResponseEntity.ok("user created");
    }

}
