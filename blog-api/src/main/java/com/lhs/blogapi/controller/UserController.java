package com.lhs.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhs.blogapi.controller.dto.ResForm;
import com.lhs.blogapi.controller.dto.ResUserInfo;
import com.lhs.blogapi.controller.dto.SignUpForm;
import com.lhs.blogapi.controller.dto.UserModifyForm;
import com.lhs.blogapi.domain.Role;
import com.lhs.blogapi.domain.User;
import com.lhs.blogapi.service.CommonService;
import com.lhs.blogapi.service.UserService;
import com.lhs.blogapi.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
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
        return createResult(201, "SUCCESS", changeUserToUserInfoClass(saveUser), "post");
    }

    // 회원정보조회
    @GetMapping("/user/{username}")
    // TODO : https://www.youtube.com/watch?v=9So7UcSRqIc&ab_channel=FlutterMentor, SearchDelegate class of Flutter
    ResponseEntity<ResForm<ResUserInfo>> findUser(@PathVariable String username) {
        User findUser = userService.findOneUser(username);
        if(findUser == null){
            return createResult(200, "NO_RECORD", null, "get");
        }
        return createResult(200, "SUCCESS", changeUserToUserInfoClass(findUser), "get");
    }

    // 회원전체조회
    @GetMapping("/user")
    ResponseEntity<ResForm<List<ResUserInfo>>> findAllUser(){
        List<User> findAllUser = userService.findAllUser();
        List<ResUserInfo> resUserInfoList = findAllUser.stream().map(this::changeUserToUserInfoClass).collect(Collectors.toList());
        return createResult(200, "SUCCESS", resUserInfoList, "get");
    }

    // 회원 역할 변경
    @PutMapping("/admin/auth/{userId}/{role}")
    ResponseEntity<ResForm<ResUserInfo>> changeAuth(@PathVariable Long userId, @PathVariable String role){
        User updateRoleUser = userService.changeAuth(userId, role);
        return createResult(200, "SUCCESS", changeUserToUserInfoClass(updateRoleUser), "put");
    }

    // 회원 정보 변경
    @PutMapping("/user/{userId}")
    ResponseEntity<ResForm<ResUserInfo>> changeUserInfo(@PathVariable Long userId, @RequestBody UserModifyForm form, HttpServletRequest request) throws AuthenticationException{
        CommonService commonService = new CommonService(userService);
        // only account owner or manager, admin can change
        User account = commonService.checkAuthenticationInfo(request).orElseThrow(() -> new AuthenticationException("Authentication error"));

        boolean isManagerOrAdmin = !account.getRoles().equals(Role.ROLE_USER);
        if(!userId.equals(account.getId()) && !isManagerOrAdmin){
            throw new IllegalArgumentException("Account Owner or Manager, Admin only can change user information");
        }

        User updateUserInfo = userService.changeUserInfo(userId, form);

        return createResult(200, "SUCCESS", changeUserToUserInfoClass(updateUserInfo), "put");
    }

    // 회원탈퇴
    @DeleteMapping("/user/{userId}")
    ResponseEntity<ResForm<String>> deleteUser(@PathVariable Long userId, HttpServletRequest request) throws AuthenticationException {
        CommonService commonService = new CommonService(userService);
        User account = commonService.checkAuthenticationInfo(request).orElseThrow(()->new AuthenticationException("Authentication error"));

        if(!userId.equals(account.getId()) && account.getRoles().equals(Role.ROLE_USER)){
            throw new IllegalArgumentException("Account Owner or Manager, Admin only can delete user information");
        }

        userService.deleteUser(userId);
        return createResult(200, "SUCCESS", String.format("회원삭제 완료 userId : %s", userId), "delete");
    }

    <T> ResponseEntity<ResForm<T>> createResult(int code, String msg, T data, String mappingType){
        String headerKey = "Content-Type";
        String headerValue = "application/json; charset=UTF-8";
        if("post".equals(mappingType)){
            return ResponseEntity.created(URI.create("")).header(headerKey, headerValue).body(new ResForm<>(code, msg, data));
        }
        return ResponseEntity.ok().header(headerKey, headerValue).body(new ResForm<>(code, msg, data));
    }

    // refresh 토큰
    @GetMapping("/token/refresh")
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, AuthenticationException {
        log.info("refresh token call");
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        CommonService commonService = new CommonService(userService);
        User user = commonService.checkAuthenticationInfo(request).orElseThrow(() -> new AuthenticationException(""));
        String refresh_token = authorizationHeader.substring(Utils.BEARER.length());

        String access_token = commonService.reIssueToken(request, user);

        log.info("access token re-issue completed");

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);

        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);

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
