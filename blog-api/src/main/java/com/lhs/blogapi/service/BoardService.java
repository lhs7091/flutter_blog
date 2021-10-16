package com.lhs.blogapi.service;

import com.lhs.blogapi.controller.dto.BoardForm;
import com.lhs.blogapi.domain.Board;
import com.lhs.blogapi.domain.User;
import com.lhs.blogapi.repository.BoardRepository;
import com.lhs.blogapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
}
