package com.lhs.blogapi.controller.dto;

import com.lhs.blogapi.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResUserInfo {

    private Long id;

    private String username;

    private String email;

    private Role role;
}
