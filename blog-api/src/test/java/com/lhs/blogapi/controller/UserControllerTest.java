package com.lhs.blogapi.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhs.blogapi.controller.dto.SignUpForm;
import com.lhs.blogapi.controller.dto.UserModifyForm;
import com.lhs.blogapi.domain.Role;
import com.lhs.blogapi.domain.User;
import com.lhs.blogapi.repository.UserRepository;
import com.lhs.blogapi.service.UserService;
import com.lhs.blogapi.util.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clearDB() {
        userRepository.deleteAll();
    }

    @Test
    void 회원가입() throws Exception {
        SignUpForm signUpForm = new SignUpForm("test", "test@test.com", "test");
        String content = objectMapper.writeValueAsString(signUpForm);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/signUp")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").exists());
    }

    @Test
    void 회원가입_중복체크에러() throws Exception {
        User user1 = new User(null, "aaa", "aaa@aaa.com", "aaa", Role.ROLE_USER, null, null);
        User saveUser1 = userService.signUp(user1);
        SignUpForm signUpForm = new SignUpForm("aaa", "aaa@aaa.com", "aaa");
        String content = objectMapper.writeValueAsString(signUpForm);

        ResultActions res = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/signUp")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        assertThat(res.andReturn().getResolvedException().getMessage())
                .isEqualTo("username 또는 email이 이미 존재합니다.");
    }

    @Test
    void 회원역할변경() throws Exception {
        User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
        User saveUser = userService.signUp(user);
        String url = String.format("/api/v1/admin/auth/%s/%s", saveUser.getId(), Role.ROLE_MANAGER.name());
        String token = createToken("admin", Role.ROLE_ADMIN);

        mockMvc.perform(MockMvcRequestBuilders.put(url).header(AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value(Role.ROLE_MANAGER.name()))
                .andReturn();
    }

    @Test
    void 회원역할변경_권한없음() throws Exception {
        User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
        User saveUser = userService.signUp(user);
        String url = String.format("/api/v1/admin/auth/%s/%s", saveUser.getId(), Role.ROLE_MANAGER.name());
        String token = createToken("manager", Role.ROLE_MANAGER);

        mockMvc.perform(MockMvcRequestBuilders.put(url).header(AUTHORIZATION, token))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void 회원역할변경_유저존재하지않음() throws Exception {
        User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
        User saveUser = userService.signUp(user);
        String url = String.format("/api/v1/admin/auth/%s/%s", saveUser.getId() + 1, Role.ROLE_MANAGER.name());
        String token = createToken("admin", Role.ROLE_ADMIN);

        ResultActions res = mockMvc.perform(put(url).header(AUTHORIZATION, token))
                .andExpect(status().isInternalServerError())
                .andDo(log());

        assertThat("해당 유저 또는 role이 존재하지 않습니다.")
                .isEqualTo(res.andReturn().getResolvedException().getMessage());
    }

    @Test
    void 회원역할변경_역할존재하지않음() throws Exception {
        User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
        User saveUser = userService.signUp(user);
        String url = String.format("/api/v1/admin/auth/%s/%s", saveUser.getId(), "ROLE_USE");
        String token = createToken("admin", Role.ROLE_ADMIN);

        ResultActions res = mockMvc.perform(put(url).header(AUTHORIZATION, token))
                .andExpect(status().isInternalServerError())
                .andDo(log());

        assertThat("해당 유저 또는 role이 존재하지 않습니다.")
                .isEqualTo(res.andReturn().getResolvedException().getMessage());
    }

    @Test
    void 회원정보변경_비밀번호제외() throws Exception {
        // given
        User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
        User saveUser = userService.signUp(user);

        String url = String.format("/api/v1/user/%s", saveUser.getId());
        UserModifyForm userModifyForm = new UserModifyForm("aaa", "aaa@aaa.com", "test", "");
        String content = objectMapper.writeValueAsString(userModifyForm);

        String token = createToken("test", Role.ROLE_USER);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders
                        .put(url)
                        .header(AUTHORIZATION, token)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(userModifyForm.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(userModifyForm.getEmail()));
    }

    @Test
    void 회원정보변경_비밀번호포함() throws Exception {
        // given
        User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
        User saveUser = userService.signUp(user);

        String url = String.format("/api/v1/user/%s", saveUser.getId());
        UserModifyForm userModifyForm = new UserModifyForm("aaa", "aaa@aaa.com", "test", "aaa");
        String content = objectMapper.writeValueAsString(userModifyForm);

        String token = createToken("test", Role.ROLE_USER);

        // when, then
        ResultActions res = mockMvc.perform(put(url)
                .header(AUTHORIZATION, token)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        res.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(userModifyForm.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(userModifyForm.getEmail()));

//        User findUser = userRepository.findById(saveUser.getId()).orElse(null);
//        assertEquals("aaa", findUser.getPassword());
//        assertNotEquals("test", findUser.getPassword());
    }

    @Test
    void 회원정보변경_비밀번호에러() throws Exception {
        // given
        User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
        User saveUser = userService.signUp(user);

        String url = String.format("/api/v1/user/%s", saveUser.getId());
        UserModifyForm userModifyForm = new UserModifyForm("aaa", "aaa@aaa.com", "aaa", "");
        String content = objectMapper.writeValueAsString(userModifyForm);

        String token = createToken("test", Role.ROLE_USER);

        // when, then
        ResultActions res = mockMvc.perform(put(url)
                        .header(AUTHORIZATION, token)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print());

        assertThat(res.andReturn().getResolvedException().getMessage())
                .isEqualTo("비밀번호가 잘못되었습니다.");
    }

    @Test
    void 회원정보변경_중복체크에러() throws Exception {
        // given
        User user1 = new User(null, "aaa", "aaa@aaa.com", "aaa", Role.ROLE_USER, null, null);
        User saveUser1 = userService.signUp(user1);

        User user2 = new User(null, "bbb", "bbb@bbb.com", "bbb", Role.ROLE_USER, null, null);
        User saveUser2 = userService.signUp(user2);

        String url = String.format("/api/v1/user/%s", saveUser1.getId());
        UserModifyForm userModifyForm = new UserModifyForm(user2.getUsername(), user2.getEmail(), "aaa", "");
        String content = objectMapper.writeValueAsString(userModifyForm);

        String token = createToken("aaa", Role.ROLE_USER);

        // when, then
        ResultActions res = mockMvc.perform(put(url)
                        .header(AUTHORIZATION, token)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print());

        assertThat(res.andReturn().getResolvedException().getMessage())
                .isEqualTo("username 또는 email이 이미 존재합니다.");
    }

    @Test
    void 회원삭제() throws Exception {
        // given
        User user = new User(null, "aaa", "aaa@aaa.com", "aaa", Role.ROLE_USER, null, null);
        User saveUser = userService.signUp(user);
        String url = String.format("/api/v1/user/%s", saveUser.getId());
        String token = createToken("aaa", Role.ROLE_USER);

        // when
        mockMvc.perform(delete(url).header(AUTHORIZATION, token))
                .andExpect(status().isOk());

        // then
        User findUser = userService.findOneUser(saveUser.getUsername());
        assertThat(findUser).isEqualTo(null);

    }

    @Test
    void 회원삭제_미회원() throws Exception {
        // given
        User user = new User(null, "aaa", "aaa@aaa.com", "aaa", Role.ROLE_USER, null, null);
        User saveUser = userService.signUp(user);
        String url = String.format("/api/v1/user/%s", saveUser.getId() + 1);

        String token = createToken("aaa", Role.ROLE_USER);

        // when
        ResultActions res = mockMvc.perform(delete(url).header(AUTHORIZATION, token))
                .andExpect(status().isInternalServerError());

        // then
        assertThat(res.andReturn().getResolvedException().getMessage())
                .isEqualTo("해당 회원이 존재하지 않습니다.");
    }

    public String createToken(String username, Role role) {
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

    @Test
    void 비밀번호암호화(){
        String encodePass = passwordEncoder.encode("test");
        System.out.println(passwordEncoder.matches("test", encodePass));
    }

}