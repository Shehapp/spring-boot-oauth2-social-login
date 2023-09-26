package com.example.OAuth2Login.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestLogin {
    String email;
    String password;
}
