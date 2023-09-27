package com.example.OAuth2Login.config.aouth2;

import com.example.OAuth2Login.util.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class HttpCookieAuthorizeRequest implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    public static String REDIRECT_URL_COOKIE_NAME="redirect_uri";
    public static String Authorization_Request_Cookie_Name="oauth2_auth_request";
    private static final Long cookieExpireSeconds=180L;
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {

        try {
           return CookieUtils.deserialize(CookieUtils.getCookie(request,Authorization_Request_Cookie_Name).get(),OAuth2AuthorizationRequest.class);
        }catch (Exception e){
            return null;
        }

    }

    // call when before redirect to authorization server to get authorization code
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {

        removeCookies(request,response);

        if(authorizationRequest==null){
            return;
        }


        // add authorization request to cookie
        CookieUtils.addCookie(
                response,
                Authorization_Request_Cookie_Name,
                CookieUtils.serialize(authorizationRequest),
                cookieExpireSeconds
                );
        System.out.println("1");
        System.out.println(request.getParameter(REDIRECT_URL_COOKIE_NAME));
        System.out.println("1");

        // add redirect url to cookie
        String redirectUrl=request.getParameter(REDIRECT_URL_COOKIE_NAME);
        if(redirectUrl!=null) {
            CookieUtils.addCookie(response,
                    REDIRECT_URL_COOKIE_NAME,
                    redirectUrl,
                    cookieExpireSeconds);
        }

    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return loadAuthorizationRequest(request);
    }


    public static void removeCookies(HttpServletRequest request, HttpServletResponse response){
        CookieUtils.deleteCookie(request,response,Authorization_Request_Cookie_Name);
        CookieUtils.deleteCookie(request,response,REDIRECT_URL_COOKIE_NAME);
    }
}
