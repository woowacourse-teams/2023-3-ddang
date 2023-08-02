package com.ddang.ddang.chat.presentation;

import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.UserNotFoundException;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ChatRoomController.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ChatRoomControllerTest {

    @MockBean
    MessageService messageService;

    @Autowired
    ChatRoomController chatRoomController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(chatRoomController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    void 메시지를_생성한다() throws Exception {
        // given
        final String contents = "메시지 내용";
        final CreateMessageRequest request = new CreateMessageRequest(
                1L,
                1L,
                contents
        );

        given(messageService.create(any(CreateMessageDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/chattings/1/messages")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request)))
               .andExpectAll(
                       status().isCreated(),
                       header().string(HttpHeaders.LOCATION, is("/chattings/1/messages/1")),
                       jsonPath("$.id", is(1L), Long.class)
               );
    }

    @Test
    void 채팅방이_없는_경우_메시지_생성시_404를_반환한다() throws Exception {
        // given
        final Long invalidChatRoomId = -999L;
        final String contents = "메시지 내용";
        final CreateMessageRequest request = new CreateMessageRequest(1L, 1L, contents);

        final ChatRoomNotFoundException chatRoomNotFoundException = new ChatRoomNotFoundException("지정한 아이디에 대한 채팅방을 찾을 수 없습니다.");
        given(messageService.create(CreateMessageDto.from(invalidChatRoomId, request)))
                .willThrow(chatRoomNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings/{chatRoomId}/messages", invalidChatRoomId)
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(chatRoomNotFoundException.getMessage()))
               );
    }

    @Test
    void 발신자가_없는_경우_메시지_생성시_404를_반환한다() throws Exception {
        // given
        final Long invalidWriterId = -999L;
        final Long chatRoomId = 1L;
        final String contents = "메시지 내용";
        final CreateMessageRequest request = new CreateMessageRequest(invalidWriterId, 1L, contents);

        final UserNotFoundException userNotFoundException = new UserNotFoundException("지정한 아이디에 대한 발신자를 찾을 수 없습니다.");
        given(messageService.create(CreateMessageDto.from(chatRoomId, request)))
                .willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(post("/chattings/{chatRoomId}/messages", chatRoomId)
                       .content(objectMapper.writeValueAsString(request))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }
}
