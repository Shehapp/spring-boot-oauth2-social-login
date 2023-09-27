package com.example.OAuth2Login.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

   public static Optional<String> getCookie(HttpServletRequest request, String name){
       Cookie[] cookies= request.getCookies();
       if(cookies!=null){
           for(Cookie cookie:cookies){
               if(cookie.getName().equals(name))
                   return Optional.of(cookie.getValue());
           }
       }
       return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name,String value, Long maxAge){
       Cookie cookie=new Cookie(name,value);
       cookie.setPath("/");
       cookie.setHttpOnly(true);
       cookie.setMaxAge(Math.toIntExact(maxAge));
       response.addCookie(cookie);
    }
    public static void deleteCookie(HttpServletRequest request,HttpServletResponse response, String name){

       // delete the cookie from a user's browser
        Cookie[] cookies= request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals(name)){
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object obj){
        return Base64
                .getUrlEncoder()
                .encodeToString(
                        SerializationUtils
                                .serialize(obj)
                );
    }
    public static <T> T deserialize(String decode, Class<T> tClass){
        return tClass.cast(SerializationUtils.deserialize( Base64
                .getUrlDecoder().decode(decode)));
    }
}
