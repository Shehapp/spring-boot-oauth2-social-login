package com.example.OAuth2Login.config.aouth2;

import com.example.OAuth2Login.config.aouth2.userinfo.UserInfo;
import com.example.OAuth2Login.config.aouth2.userinfo.UserInfoFactory;
import com.example.OAuth2Login.entity.AuthProvider;
import com.example.OAuth2Login.entity.GlobalUser;
import com.example.OAuth2Login.entity.Role;
import com.example.OAuth2Login.entity.User;
import com.example.OAuth2Login.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User= super.loadUser(userRequest);



        UserInfo userInfo= UserInfoFactory.getUserInfo(userRequest.getClientRegistration().getRegistrationId(),oAuth2User.getAttributes());



        Optional<User> userOptional=userRepository.findByEmail(userInfo.getEmail());

        List<SimpleGrantedAuthority> authorities=new ArrayList<>();

        if(userOptional.isPresent()){
            updateUser(userOptional.get(),userInfo);
            authorities.add(new SimpleGrantedAuthority(userOptional.get().getRole().toString()));

        }else {
            saveUser(userInfo,userRequest.getClientRegistration().getRegistrationId());
            authorities.add(new SimpleGrantedAuthority(Role.USER.toString()));
        }

        return GlobalUser
                .builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .attributes(oAuth2User.getAttributes())
                .authorities(authorities)
                .build();
    }
    void saveUser(UserInfo userInfo, String registrationId){
        User user=User
                .builder()
                .name(userInfo.getName())
                .email(userInfo.getEmail())
                .imgUrl(userInfo.getImageUrl())
                .role(Role.USER)
                .authProvider((registrationId.equals("google"))?AuthProvider.GOOGLE:AuthProvider.GITHUB)
                .build();
        userRepository.save(user);
    }

    void updateUser(User user,UserInfo userInfo){
        if(userInfo.getName()!=null)
            user.setName(userInfo.getName());
        if(userInfo.getImageUrl()!=null)
            user.setImgUrl(userInfo.getImageUrl());
        userRepository.save(user);
    }
}
