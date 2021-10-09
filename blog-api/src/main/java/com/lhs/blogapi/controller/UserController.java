package com.lhs.blogapi.controller;

import com.lhs.blogapi.controller.dto.ResForm;
import com.lhs.blogapi.controller.dto.ResUserInfo;
import com.lhs.blogapi.controller.dto.SignUpForm;
import com.lhs.blogapi.controller.dto.UserModifyForm;
import com.lhs.blogapi.domain.Role;
import com.lhs.blogapi.domain.User;
import com.lhs.blogapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    ResUserInfo changeUserToUserInfoClass(User user){
        return new ResUserInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles()
        );
    }
}
