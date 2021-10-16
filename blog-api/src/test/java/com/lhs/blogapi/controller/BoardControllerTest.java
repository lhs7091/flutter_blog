package com.lhs.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhs.blogapi.controller.dto.BoardForm;
import com.lhs.blogapi.domain.Board;
import com.lhs.blogapi.domain.Role;
import com.lhs.blogapi.domain.User;
import com.lhs.blogapi.repository.BoardRepository;
import com.lhs.blogapi.repository.UserRepository;
import com.lhs.blogapi.service.BoardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.lhs.blogapi.common.CommonUtil.createToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {
    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    @Transactional
    void 목록가져오기() throws Exception {
        // given
        User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
        userRepository.save(user);

        Board board = new Board(null, "title1", "content1", user, null, null, null);
        Board saveBoard = boardRepository.save(board);

        String url = "/api/v1/board";
        String token = createToken("test", Role.ROLE_USER);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders
                .get(url)
                .header(AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(saveBoard.getTitle()))
                .andExpect(jsonPath("$[0].content").value(saveBoard.getContent()))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @Transactional
    void 글작성하기() throws Exception {
        // given
        User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
        User saveUser = userRepository.save(user);

        BoardForm boardForm = new BoardForm("title1", "content1");

        String content = objectMapper.writeValueAsString(boardForm);

        String url = String.format("/api/v1/board/%s", saveUser.getId());
        String token = createToken("test", Role.ROLE_USER);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .header(AUTHORIZATION, token)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.title").value(boardForm.getTitle()))
                .andExpect(jsonPath("$.data.content").value(boardForm.getContent()));
    }

    @Test
    public void 글작성하기_없는회원() throws Exception {
        // given
        User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
        User saveUser = userRepository.save(user);

        BoardForm boardForm = new BoardForm("title1", "content1");

        String content = objectMapper.writeValueAsString(boardForm);

        String url = String.format("/api/v1/board/%s", saveUser.getId()+1);
        String token = createToken("test", Role.ROLE_USER);

        // when, then
        ResultActions res = mockMvc.perform(MockMvcRequestBuilders
                        .post(url)
                        .header(AUTHORIZATION, token)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        assertThat(Objects.requireNonNull(res.andReturn().getResolvedException()).getMessage())
                .isEqualTo("Illegal User Information");
    }

}