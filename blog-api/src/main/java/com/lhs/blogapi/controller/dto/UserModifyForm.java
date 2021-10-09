package com.lhs.blogapi.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyForm {

    private String username;

    private String email;

    private String currentPassword;

    private String newPassword;
}
