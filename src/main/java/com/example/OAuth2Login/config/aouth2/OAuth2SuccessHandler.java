package com.example.OAuth2Login.config.aouth2;

import com.example.OAuth2Login.config.jwt.JwtService;
import com.example.OAuth2Login.entity.GlobalUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String token = jwtService.generateToken((GlobalUser)authentication.getPrincipal());
        System.out.println(token);
        getRedirectStrategy().sendRedirect(request,response,"/home?token="+token);
    }
}
