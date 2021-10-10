package com.lhs.blogapi.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhs.blogapi.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    public final ObjectMapper om = new ObjectMapper();

    /**
     * 처음 /api/login으로 요청하면 이쪽으로 오게 된다.
     * 그럼 json을 통해서 온 username과 password를 받아서 userservice의 loadUserByUsername메서드를 호출한다.
     * 호출 하면 그 결과로 token을 생성한다.
     * 그리고 successfulAuthentication으로 가서 jwt토큰을 발행한다.
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication run");
        ServletInputStream inputStream = null;
        String body = null;
        LoginReq loginReq = null;
        UsernamePasswordAuthenticationToken authenticationToken = null;

        try {
            inputStream = request.getInputStream();
            body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            loginReq = om.readValue(body, LoginReq.class);
            authenticationToken = new UsernamePasswordAuthenticationToken(loginReq.username, loginReq.password);
            log.info("attemptAuthentication authenticationToken issued");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authenticationManager.authenticate(authenticationToken);
    }


    /**
     * 로그인 완료했기때문에 jwt 토큰을 발행한다.
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("successfulAuthentication run");

        User principal = (User) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC512(Utils.SECRET_KEY);
        String access_token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + Utils.ACC_EXPIRE))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        log.info("successfulAuthentication access token issue complete");

        String refresh_token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + Utils.REF_EXPIRE))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        log.info("successfulAuthentication refresh token issue complete");

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);

        om.writeValue(response.getOutputStream(), tokens);
    }
}


