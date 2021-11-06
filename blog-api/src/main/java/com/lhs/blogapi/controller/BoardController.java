package com.lhs.blogapi.controller;

import com.lhs.blogapi.controller.dto.BoardForm;
import com.lhs.blogapi.controller.dto.ResForm;
import com.lhs.blogapi.domain.Board;
import com.lhs.blogapi.domain.User;
import com.lhs.blogapi.service.BoardService;
import com.lhs.blogapi.service.CommonService;
import com.lhs.blogapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.lhs.blogapi.util.Utils.headerKey;
import static com.lhs.blogapi.util.Utils.headerValue;

@RestController
@RequestMapping("/api/v1/board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

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
    @PutMapping("/{boardId}")
    public ResponseEntity<ResForm<Board>> updateBoard(@PathVariable Long boardId, @RequestBody BoardForm boardForm, HttpServletRequest request) throws AuthenticationException {
        CommonService commonService = new CommonService(userService);

        User account = commonService.checkAuthenticationInfo(request).orElseThrow(() -> new AuthenticationException("Authentication Error"));

        Board updateBoard = boardService.updateBoard(boardId, boardForm, account);
        return createResult(200, "SUCCESS", updateBoard, "put");
    }

    // 글 삭제하기
    @DeleteMapping("/{boardId}")
    public ResponseEntity<ResForm<Board>> updateBoard(@PathVariable Long boardId, HttpServletRequest request) throws AuthenticationException {
        CommonService commonService = new CommonService(userService);
        User account = commonService.checkAuthenticationInfo(request).orElseThrow(() -> new AuthenticationException("Authentication Error"));

        boardService.deleteBoard(boardId, account);

        return createResult(200, "SUCCESS", null, "delete");
    }

    <T> ResponseEntity<ResForm<T>> createResult(int code, String msg, T data, String mappingType){
        if("post".equals(mappingType)){
            return ResponseEntity.created(null).header(headerKey, headerValue).body(new ResForm<>(code, msg, data));
        }
        return ResponseEntity.ok().header(headerKey, headerValue).body(new ResForm<>(code, msg, data));
    }
}
