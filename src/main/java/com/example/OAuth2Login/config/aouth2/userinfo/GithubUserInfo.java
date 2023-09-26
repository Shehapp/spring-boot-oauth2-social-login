package com.example.OAuth2Login.config.aouth2.userinfo;


import java.util.Map;


public class GithubUserInfo extends UserInfo {
    public GithubUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        return (String) attributes.get("login");
    }

    @Override
    public String getEmail() {
        if(null== (String) attributes.get("email"))
            return getName()+"@gmail.com";
        else
            return (String) attributes.get("email");

    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("avatar_url");
    }
}
