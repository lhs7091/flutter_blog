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

import static com.lhs.blogapi.util.Utils.headerKey;
import static com.lhs.blogapi.util.Utils.headerValue;

@RestController
@RequestMapping("/api/v1/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 목록보기
    @GetMapping("")
    public ResponseEntity<ResForm<List<Board>>> getAllPosts(){
        List<Board> boards = boardService.getAllPosts();
        return createResult(200, "SUCCESS", boards, "get");
    }

    // 글쓰기
    @PostMapping("/{userid}")
    public ResponseEntity<ResForm<Board>> writeBoard(@PathVariable Long userid, @RequestBody BoardForm boardForm){
        Board board = boardService.writeBoard(userid, boardForm);
        return createResult(200, "SUCCESS", board, "post");
    }

    // 글 수정하기
    @PutMapping("/{boardid}")
    public ResponseEntity<ResForm<Board>> updateBoard(@PathVariable Long boardid, @RequestBody BoardForm boardForm){
        Board updateBoard = boardService.updateBoard(boardid, boardForm);
        return createResult(200, "SUCCESS", updateBoard, "put");
    }

    // 글 삭제하기
    @DeleteMapping("/{boardid}")
    public ResponseEntity<ResForm<Board>> updateBoard(@PathVariable Long boardid){
        boardService.deleteBoard(boardid);
        return createResult(200, "SUCCESS", null, "delete");
    }
    <T> ResponseEntity<ResForm<T>> createResult(int code, String msg, T data, String mappingType){
        if("post".equals(mappingType)){
            return ResponseEntity.created(null).header(headerKey, headerValue).body(new ResForm<>(code, msg, data));
        }
        return ResponseEntity.ok().header(headerKey, headerValue).body(new ResForm<>(code, msg, data));
    }
}
