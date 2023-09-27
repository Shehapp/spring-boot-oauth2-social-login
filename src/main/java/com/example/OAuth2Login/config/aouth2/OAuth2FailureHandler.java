package com.example.OAuth2Login.config.aouth2;

import com.example.OAuth2Login.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler{
    @Value("${spring.app.authorized_redirect}")
    private String defaultRedirect;
    public static String REDIRECT_URL_COOKIE_NAME="redirect_uri";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String redirectUrl=defaultRedirect;


        if(CookieUtils.getCookie(request,REDIRECT_URL_COOKIE_NAME).isPresent()){
            redirectUrl=CookieUtils.getCookie(request,REDIRECT_URL_COOKIE_NAME).get();
        }

        HttpCookieAuthorizeRequest.removeCookies(request,response);

        String targetUrl= UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("error","failed")
                .build().toString();

        getRedirectStrategy().sendRedirect(request,response,targetUrl);

    }
}
