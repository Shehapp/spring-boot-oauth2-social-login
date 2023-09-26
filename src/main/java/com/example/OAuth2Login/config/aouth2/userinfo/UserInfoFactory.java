package com.example.OAuth2Login.config.aouth2.userinfo;

import com.example.OAuth2Login.entity.AuthProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public class UserInfoFactory {


    public static UserInfo getUserInfo(String registrationId, Map<String, Object> attributes){
        if(registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.name())){
            return new GoogleUserInfo(attributes);
        }
        else if(registrationId.equalsIgnoreCase(AuthProvider.GITHUB.name())){
            return new GithubUserInfo(attributes);
        }
        else{
            throw new RuntimeException("registrationId is not valid");
        }
    }
}
