package com.lhs.blogapi.exception;

import com.lhs.blogapi.controller.dto.ResForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ResponseException {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    ResponseEntity<ResForm<String>> handlerIllegalArgumentException(IllegalArgumentException e){
        return createResult(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    ResponseEntity<ResForm<String>> handlerNoSuchElementException(NoSuchElementException e){
        return createResult(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    ResponseEntity<ResForm<String>> handlerUsernameNotFoundException(UsernameNotFoundException e){
        return createResult(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name(), e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    ResponseEntity<ResForm<String>> handlerAuthenticationException(AuthenticationException e){
        return createResult(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.name(), e.getMessage());
    }

    <T> ResponseEntity<ResForm<T>> createResult(int code, String msg, T data){
        String headerKey = "Content-Type";
        String headerValue = "application/json; charset=UTF-8";

        if (code == HttpStatus.BAD_REQUEST.value()){
            return ResponseEntity.badRequest().header(headerKey, headerValue).body(new ResForm<>(code, msg, data));
        }else if (code == HttpStatus.NOT_FOUND.value()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).header(headerKey, headerValue).body(new ResForm<>(code, msg, data));
        }else if(code == HttpStatus.UNAUTHORIZED.value()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).header(headerKey, headerValue).body(new ResForm<>(code, msg, data));
        }
        return null;
    }
}
