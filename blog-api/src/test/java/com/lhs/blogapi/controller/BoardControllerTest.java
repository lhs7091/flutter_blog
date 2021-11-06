package com.lhs.blogapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lhs.blogapi.controller.dto.BoardForm;
import com.lhs.blogapi.domain.Board;
import com.lhs.blogapi.domain.Role;
import com.lhs.blogapi.domain.User;
import com.lhs.blogapi.repository.UserRepository;
import com.lhs.blogapi.service.BoardService;
import com.lhs.blogapi.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.Objects;

import static com.lhs.blogapi.common.CommonUtil.createToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BoardControllerTest {
    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @AfterEach
    void clearDB() {
        userRepository.deleteAll();
    }

    @Test
    void 목록가져오기() throws Exception {
        // given
        User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
        User saveUser = userService.signUp(user);

        BoardForm boardForm1 = new BoardForm("title1", "content1");
        Board saveBoard = boardService.writeBoard(saveUser.getId(), boardForm1);

        BoardForm boardForm2 = new BoardForm("title2", "content2");
        boardService.writeBoard(saveUser.getId(), boardForm2);

        String url = "/api/v1/board";
        String token = createToken("test", Role.ROLE_USER);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .header(AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value(boardForm1.getTitle()))
                .andExpect(jsonPath("$.data[0].content").value(boardForm1.getContent()))
                .andExpect(jsonPath("$.data[1].title").value(boardForm2.getTitle()))
                .andExpect(jsonPath("$.data[1].content").value(boardForm2.getContent()))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Nested
    public class write_board {

        @Test
        void 글작성하기() throws Exception {
            // given
            User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
            User saveUser = userService.signUp(user);

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
            User saveUser = userService.signUp(user);

            BoardForm boardForm = new BoardForm("title1", "content1");

            String content = objectMapper.writeValueAsString(boardForm);

            String url = String.format("/api/v1/board/%s", saveUser.getId() + 1);
            String token = createToken("test", Role.ROLE_USER);

            // when, then
            ResultActions res = mockMvc.perform(MockMvcRequestBuilders
                            .post(url)
                            .header(AUTHORIZATION, token)
                            .content(content)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            assertThat(Objects.requireNonNull(res.andReturn().getResolvedException()).getMessage())
                    .isEqualTo("Illegal User Information");
        }
    }

    @Nested
    public class update_board {

        @Test
        void 글수정하기() throws Exception {
            // given
            User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
            User saveUser = userService.signUp(user);

            BoardForm boardForm = new BoardForm("title1", "content1");
            Board saveBoard = boardService.writeBoard(saveUser.getId(), boardForm);

            String token = createToken(user.getUsername(), user.getRoles());
            String url = String.format("/api/v1/board/%s", saveBoard.getId());
            // when
            BoardForm updateForm = new BoardForm("title2", "content2");
            String content = objectMapper.writeValueAsString(updateForm);

            // then
            mockMvc.perform(MockMvcRequestBuilders
                            .put(url)
                            .header(AUTHORIZATION, token)
                            .content(content)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.title").value(updateForm.getTitle()))
                    .andExpect(jsonPath("$.data.content").value(updateForm.getContent()));
        }

        @Test
        void 글수정하기_다른회원() throws Exception {
            // given
            User user1 = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
            User saveUser1 = userService.signUp(user1);

            User user2 = new User(null, "aaa", "aaa@aaa.com", "aaa", Role.ROLE_USER, null, null);
            User saveUser2 = userService.signUp(user2);

            BoardForm boardForm = new BoardForm("title1", "content1");
            Board saveBoard = boardService.writeBoard(saveUser1.getId(), boardForm);

            String token = createToken(saveUser2.getUsername(), saveUser2.getRoles());
            String url = String.format("/api/v1/board/%s", saveBoard.getId());

            // when
            BoardForm updateForm = new BoardForm("title2", "content2");
            String content = objectMapper.writeValueAsString(updateForm);

            // then
            mockMvc.perform(MockMvcRequestBuilders
                            .put(url)
                            .header(AUTHORIZATION, token)
                            .content(content)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                            .isEqualTo(String.format("Account Owner only can update this Board, boardId : %s", saveBoard.getId())));

        }

        @Test
        void 글수정하기_Manager수정() throws Exception {
            // given
            User user1 = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
            User saveUser1 = userService.signUp(user1);

            User user2 = new User(null, "aaa", "aaa@aaa.com", "aaa", Role.ROLE_MANAGER, null, null);
            User saveUser2 = userService.signUp(user2);

            BoardForm boardForm = new BoardForm("title1", "content1");
            Board saveBoard = boardService.writeBoard(saveUser1.getId(), boardForm);

            String token = createToken(saveUser2.getUsername(), saveUser2.getRoles());
            String url = String.format("/api/v1/board/%s", saveBoard.getId());

            // when
            BoardForm updateForm = new BoardForm("title2", "content2");
            String content = objectMapper.writeValueAsString(updateForm);

            // then
            mockMvc.perform(MockMvcRequestBuilders
                            .put(url)
                            .header(AUTHORIZATION, token)
                            .content(content)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.title").value(updateForm.getTitle()))
                    .andExpect(jsonPath("$.data.content").value(updateForm.getContent()));
        }

        @Test
        void 글수정하기_존재하지않는글() throws Exception {
            // given
            User user1 = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
            User saveUser1 = userService.signUp(user1);

            BoardForm boardForm = new BoardForm("title1", "content1");
            Board saveBoard = boardService.writeBoard(saveUser1.getId(), boardForm);

            String token = createToken(saveUser1.getUsername(), saveUser1.getRoles());
            String url = String.format("/api/v1/board/%s", saveBoard.getId() + 1);

            // when
            BoardForm updateForm = new BoardForm("title2", "content2");
            String content = objectMapper.writeValueAsString(updateForm);

            // then
            mockMvc.perform(MockMvcRequestBuilders
                            .put(url)
                            .header(AUTHORIZATION, token)
                            .content(content)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                            .isEqualTo("Illegal Board Information"));

        }
    }

    @Nested
    public class delete_board {

        @Test
        void 글삭제하기() throws Exception {
            // given
            User user = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
            User saveUser = userService.signUp(user);

            BoardForm boardForm = new BoardForm("title1", "content1");
            Board saveBoard = boardService.writeBoard(saveUser.getId(), boardForm);

            String token = createToken(user.getUsername(), user.getRoles());
            String url = String.format("/api/v1/board/%s", saveBoard.getId());

            // when, then
            mockMvc.perform(MockMvcRequestBuilders
                    .delete(url)
                    .header(AUTHORIZATION, token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void 글삭제하기_다른회원삭제() throws Exception {
            // given
            User user1 = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
            User saveUser1 = userService.signUp(user1);

            User user2 = new User(null, "aaa", "aaa@aaa.com", "aaa", Role.ROLE_USER, null, null);
            User saveUser2 = userService.signUp(user2);

            BoardForm boardForm = new BoardForm("title1", "content1");
            Board saveBoard = boardService.writeBoard(saveUser1.getId(), boardForm);

            String token = createToken(saveUser2.getUsername(), saveUser2.getRoles());
            String url = String.format("/api/v1/board/%s", saveBoard.getId());

            // when, then
            mockMvc.perform(MockMvcRequestBuilders
                    .delete(url)
                    .header(AUTHORIZATION, token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                            .isEqualTo(String.format("Account Owner only can update this Board, boardId : %s", saveBoard.getId())));
        }

        @Test
        void 글삭제하기_Manager강제삭제() throws Exception {
            // given
            User user1 = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
            User saveUser1 = userService.signUp(user1);

            User user2 = new User(null, "aaa", "aaa@aaa.com", "aaa", Role.ROLE_MANAGER, null, null);
            User saveUser2 = userService.signUp(user2);

            BoardForm boardForm = new BoardForm("title1", "content1");
            Board saveBoard = boardService.writeBoard(saveUser1.getId(), boardForm);

            String token = createToken(saveUser2.getUsername(), saveUser2.getRoles());
            String url = String.format("/api/v1/board/%s", saveBoard.getId());

            // when, then
            mockMvc.perform(MockMvcRequestBuilders
                    .delete(url)
                    .header(AUTHORIZATION, token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void 글삭제하기_없는글삭제() throws Exception {
            // given
            User user1 = new User(null, "test", "test@test.com", "test", Role.ROLE_USER, null, null);
            User saveUser1 = userService.signUp(user1);

            BoardForm boardForm = new BoardForm("title1", "content1");
            Board saveBoard = boardService.writeBoard(saveUser1.getId(), boardForm);

            String token = createToken(saveUser1.getUsername(), saveUser1.getRoles());
            String url = String.format("/api/v1/board/%s", saveBoard.getId()+1);

            // when, then
            mockMvc.perform(MockMvcRequestBuilders
                    .delete(url)
                    .header(AUTHORIZATION, token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                    .andExpect(result -> assertThat(result.getResolvedException().getMessage())
                            .isEqualTo("Illegal Board Information"));
        }
    }
}