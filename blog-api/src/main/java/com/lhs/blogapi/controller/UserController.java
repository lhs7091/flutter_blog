package com.lhs.blogapi.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhs.blogapi.controller.dto.ResForm;
import com.lhs.blogapi.controller.dto.ResUserInfo;
import com.lhs.blogapi.controller.dto.SignUpForm;
import com.lhs.blogapi.controller.dto.UserModifyForm;
import com.lhs.blogapi.domain.Role;
import com.lhs.blogapi.domain.User;
import com.lhs.blogapi.service.UserService;
import com.lhs.blogapi.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signUp")
    @ResponseStatus(code = HttpStatus.CREATED)
    ResponseEntity<ResForm<ResUserInfo>> signUp(@RequestBody SignUpForm form){
        User saveUser = userService.signUp(new User(null, form.getUsername(), form.getEmail(), form.getPassword(), Role.ROLE_USER, null, null));
        return createResult(1, "SUCCESS", changeUserToUserInfoClass(saveUser), "post");
    }

    // 회원정보조회
    @GetMapping("/user/{username}")
    void findUser(@PathVariable String username){
        userService.findOneUser(username);
    }

    // 회원전체조회
    @GetMapping("/user/all")
    void findAllUser(){
        userService.findAllUser();
    }

    // 회원 역할 변경
    @PutMapping("/admin/auth/{userId}/{role}")
    ResponseEntity<ResForm<ResUserInfo>> changeAuth(@PathVariable Long userId, @PathVariable String role){
        User updateRoleUser = userService.changeAuth(userId, role);
        return createResult(1, "SUCCESS", changeUserToUserInfoClass(updateRoleUser), "put");
    }

    // 회원 정보 변경
    @PutMapping("/user/{userId}")
    ResponseEntity<ResForm<ResUserInfo>> changeUserInfo(@PathVariable Long userId, @RequestBody UserModifyForm form){
        User updateUserInfo = userService.changeUserInfo(userId, form);
        return createResult(1, "SUCCESS", changeUserToUserInfoClass(updateUserInfo), "put");
    }

    // 회원탈퇴
    @DeleteMapping("/user/{userId}")
    ResponseEntity<ResForm<String>> deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
        return createResult(1, "SUCCESS", String.format("회원삭제 완료 userId : %s", userId), "delete");
    }

    <T> ResponseEntity<ResForm<T>> createResult(int code, String msg, T data, String mappingType){
        String headerKey = "Content-Type";
        String headerValue = "application/json; charset=UTF-8";
        if("post".equals(mappingType)){
            return ResponseEntity.created(null).header(headerKey, headerValue).body(new ResForm<>(code, msg, data));
        }
        return ResponseEntity.ok().header(headerKey, headerValue).body(new ResForm<>(code, msg, data));
    }

    // refresh 토큰
    @GetMapping("/token/refresh")
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException{
        log.info("refresh token call");
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(Utils.BEARER)){

            // 토큰 정보에 있는 payload를 평문으로 변환
            String refresh_token = authorizationHeader.substring(Utils.BEARER.length());
            Algorithm algorithm = Algorithm.HMAC512(Utils.SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refresh_token);

            String username = decodedJWT.getSubject();
            User user = userService.findOneUser(username);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRoles().name()));
            String access_token = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + Utils.ACC_EXPIRE))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);

            log.info("access token 재발급 완료");

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", access_token);
            tokens.put("refresh_token", refresh_token);

            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        }
    }

    ResUserInfo changeUserToUserInfoClass(User user){
        return new ResUserInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles()
        );
    }
}
