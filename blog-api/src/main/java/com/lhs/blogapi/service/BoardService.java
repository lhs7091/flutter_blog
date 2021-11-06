package com.lhs.blogapi.service;

import com.lhs.blogapi.controller.dto.BoardForm;
import com.lhs.blogapi.domain.Board;
import com.lhs.blogapi.domain.Role;
import com.lhs.blogapi.domain.User;
import com.lhs.blogapi.repository.BoardRepository;
import com.lhs.blogapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<Board> getAllPosts(){
        return boardRepository.findAll();
    }

    public Board writeBoard(Long userid, BoardForm boardForm) {
        User findUser = userRepository.findById(userid).orElse(null);

        if (findUser == null){
            throw new IllegalArgumentException("Illegal User Information");
        }

        Board saveBoard = boardRepository.save(new Board(null, boardForm.getTitle(), boardForm.getContent(), findUser, null, null, null));

        return saveBoard;
    }

    public Board updateBoard(Long boardId, BoardForm boardForm, User account) {
        Board findBoard = boardRepository.findById(boardId).orElse(null);
        if (findBoard == null){
            throw new IllegalArgumentException("Illegal Board Information");
        }

        if (findBoard.getUser().getId() != account.getId() && Role.ROLE_USER.equals(account.getRoles())){
            throw new IllegalArgumentException(String.format("Account Owner only can update this Board, boardId : %s", boardId));
        }

        findBoard.setTitle(boardForm.getTitle());
        findBoard.setContent(boardForm.getContent());
        Board updateBoard = boardRepository.save(findBoard);

        return updateBoard;
    }

    public void deleteBoard(Long boardId, User account) {
        Board findBoard = boardRepository.findById(boardId).orElse(null);

        if (findBoard == null){
            throw new IllegalArgumentException("Illegal Board Information");
        }

        if (findBoard.getUser().getId() == account.getId() || !Role.ROLE_USER.equals(account.getRoles())){
            boardRepository.delete(findBoard);
            return;
        }

        throw new IllegalArgumentException(String.format("Account Owner only can update this Board, boardId : %s", boardId));

    }
}
