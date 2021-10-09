package com.lhs.blogapi.controller.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;
}
