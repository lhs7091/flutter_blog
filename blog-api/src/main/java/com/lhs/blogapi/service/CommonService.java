package com.lhs.blogapi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lhs.blogapi.domain.User;
import com.lhs.blogapi.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class CommonService {

    private final UserService userService;
    private static Algorithm algorithm = Algorithm.HMAC512(Utils.SECRET_KEY);

    public Optional<User> checkAuthenticationInfo(HttpServletRequest request){
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(Utils.BEARER)){
            String token = authorizationHeader.substring(Utils.BEARER.length());
            return Optional.ofNullable(decodeJWT(token));
        }
        return Optional.empty();
    }

    public User decodeJWT(String token){
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);

        String username = decodedJWT.getSubject();
        return userService.findOneUser(username);
    }

    public String reIssueToken(HttpServletRequest request, User user){
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoles().name()));
        // return access token
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + Utils.ACC_EXPIRE))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }
}
