package com.example.OAuth2Login.config.jwt;

import com.example.OAuth2Login.entity.GlobalUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service

public class JwtService {

    @Value("${spring.security.jwt.secret}")
    private String secretKey;
    @Value("${spring.security.jwt.expiration}")
    private Long expireTime;
    public boolean validateToken(String jwtToken) {
        try {
            Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(jwtToken);
            return !isTokenExpired(jwtToken);
        } catch (Exception e) {
            return false;
        }
    }

    public String generateToken(UserDetails userDetails) {

        return Jwts
                .builder()
                .setClaims(mapUserDetails(userDetails))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Map<String, Object> mapUserDetails(UserDetails userDetails) {
        System.out.println(userDetails.getAuthorities());
        return Map.of(
                "email", userDetails.getUsername(),
                "authority", userDetails.getAuthorities().toArray()[0].toString(),
                "expires_data",new Date(System.currentTimeMillis()+expireTime)
        );
    }

    private boolean isTokenExpired(String jwtToken) {
        Date expiration = getClaims(jwtToken).get("expires_data", Date.class);
        return expiration.before(new Date());
    }



   public UserDetails getUserDetails(String jwtToken) {
        Claims claims = getClaims(jwtToken);
        System.out.println(claims.get("expires_data", Date.class));
       List<SimpleGrantedAuthority> authorities=new ArrayList<>();
       authorities.add(new SimpleGrantedAuthority(claims.get("authority", String.class)));
        return GlobalUser
                .builder()
                .email(claims.get("email", String.class))
                .authorities(authorities)
                .password("")
                .build();

    }

    private Claims getClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
