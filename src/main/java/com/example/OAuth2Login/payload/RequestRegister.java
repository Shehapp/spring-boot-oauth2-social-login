package com.example.OAuth2Login.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestRegister {
    private String username;
    private String password;
    private String email;
}
