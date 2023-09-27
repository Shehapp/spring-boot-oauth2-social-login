package com.example.OAuth2Login.config.aouth2;

import com.example.OAuth2Login.config.jwt.JwtService;
import com.example.OAuth2Login.entity.GlobalUser;
import com.example.OAuth2Login.util.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${spring.app.authorized_redirect}")
    private String defaultRedirect;
    public static String REDIRECT_URL_COOKIE_NAME="redirect_uri";

    @Autowired
    private  JwtService jwtService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String redirectUrl=defaultRedirect;

        if(CookieUtils.getCookie(request,REDIRECT_URL_COOKIE_NAME).isPresent()){
            redirectUrl=CookieUtils.getCookie(request,REDIRECT_URL_COOKIE_NAME).get();
        }

        URI authorizedURI=URI.create(defaultRedirect);
        URI redirectURI = URI.create(redirectUrl);

        if(!(redirectURI.getHost().equalsIgnoreCase(authorizedURI.getHost()) &&
                redirectURI.getPort()==authorizedURI.getPort()))
            throw new IllegalArgumentException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");


        super.clearAuthenticationAttributes(request);
        HttpCookieAuthorizeRequest.removeCookies(request,response);

        String token = jwtService.generateToken((GlobalUser)authentication.getPrincipal());

        String targetUrl= UriComponentsBuilder.fromUriString(redirectUrl)
                        .queryParam("token",token)
                                .build().toString();
        System.out.println(targetUrl);
        getRedirectStrategy().sendRedirect(request,response,targetUrl);
    }
}
