package com.lhs.blogapi.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.lhs.blogapi.domain.Role;
import com.lhs.blogapi.util.Utils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class CommonUtil {

    public static String createToken(String username, Role role) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        Algorithm algorithm = Algorithm.HMAC512(Utils.SECRET_KEY);
        String token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + Utils.ACC_EXPIRE))
                .withIssuer("/api/login")
                .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        return Utils.BEARER + token;
    }

}
