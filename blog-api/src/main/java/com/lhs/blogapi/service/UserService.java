package com.lhs.blogapi.service;

import com.lhs.blogapi.controller.dto.UserModifyForm;
import com.lhs.blogapi.domain.Role;
import com.lhs.blogapi.domain.User;
import com.lhs.blogapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public User signUp(User user) {
        // username, email 중복체크
        duplicationCheck(0L, user.getUsername(), user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // 회원 1명 조회
    public User findOneUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // 회원 전체 조회
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    // 회원 역할 변경
    public User changeAuth(Long userId, String role) {
        // 유저 존재 확인
        User findUser = userRepository.findById(userId).orElse(null);
        // role 유효성 확인
        boolean isEnum = Arrays.stream(Role.values()).anyMatch((t) -> t.name().equals(role));

        if (findUser == null || !isEnum) {
            throw new NoSuchElementException("해당 유저 또는 role이 존재하지 않습니다.");
        }

        // 변경 role 반영
        findUser.setRoles(Role.valueOf(role));

        return userRepository.save(findUser);
    }

    // 회원 정보변경
    public User changeUserInfo(Long userId, UserModifyForm form) {
        User findUser = userRepository.findById(userId).orElse(null);

        if (findUser == null){
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        duplicationCheck(userId, form.getUsername(), form.getEmail());

        boolean isPasswordMatch = passwordEncoder.matches(form.getCurrentPassword(), findUser.getPassword());
        if(!isPasswordMatch){
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다.");
        }

        if(form.getUsername() != null && !form.getUsername().isEmpty()){
            findUser.setUsername(form.getUsername());
        }

        if(form.getEmail() != null && !form.getEmail().isEmpty()){
            findUser.setEmail(form.getEmail());
        }

        if(form.getNewPassword() != null && !form.getNewPassword().isEmpty()){
            findUser.setPassword(passwordEncoder.encode(form.getNewPassword()));
        }

        return userRepository.save(findUser);

    }

    // 회원탈퇴
    public void deleteUser(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isEmpty()){
            throw new IllegalArgumentException("해당 회원이 존재하지 않습니다.");
        }
        userRepository.deleteById(userId);
    }

    // username, email 중복체크
    public void duplicationCheck(Long userId, String username, String email){
        User myInfo = userRepository.findById(userId).orElse(new User());

        if ((userRepository.findByUsername(username).isPresent() && !username.equals(myInfo.getUsername())) ||
                (userRepository.findByEmail(email).isPresent() && !email.equals(myInfo.getEmail()))) {

            throw new IllegalArgumentException("username 또는 email이 이미 존재합니다.");
        }
    }

    // 로그인하기
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user == null){
            log.error("회원없음");
            throw new UsernameNotFoundException("회원없음");
        }

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoles().name()));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
