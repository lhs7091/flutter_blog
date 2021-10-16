package com.lhs.blogapi.controller;

import com.lhs.blogapi.controller.dto.BoardForm;
import com.lhs.blogapi.controller.dto.ResForm;
import com.lhs.blogapi.domain.Board;
import com.lhs.blogapi.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 목록보기
    @GetMapping("")
    public List<Board> getAllPosts(){
        return boardService.getAllPosts();
    }

    // 글쓰기
    @PostMapping("/{userid}")
    public ResponseEntity<ResForm<Board>> writeBoard(@PathVariable Long userid, @RequestBody BoardForm boardForm){
        Board board = boardService.writeBoard(userid, boardForm);
        return createResult(200, "SUCCESS", board, "post");
    }

    <T> ResponseEntity<ResForm<T>> createResult(int code, String msg, T data, String mappingType){
        String headerKey = "Content-Type";
        String headerValue = "application/json; charset=UTF-8";
        if("post".equals(mappingType)){
            return ResponseEntity.created(null).header(headerKey, headerValue).body(new ResForm<>(code, msg, data));
        }
        return ResponseEntity.ok().header(headerKey, headerValue).body(new ResForm<>(code, msg, data));
    }
}
