package com.lhs.blogapi.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResForm<T> {

    private int code;
    private String msg;
    private T data;
}
