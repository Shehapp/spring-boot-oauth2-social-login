package com.example.OAuth2Login.config.aouth2.userinfo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public abstract class UserInfo {

    Map<String, Object> attributes;
    public UserInfo( Map<String, Object> attributes){
        this.attributes=attributes;
    }
   public abstract String getName();
   public abstract String getEmail();
   public abstract String getImageUrl();
}
