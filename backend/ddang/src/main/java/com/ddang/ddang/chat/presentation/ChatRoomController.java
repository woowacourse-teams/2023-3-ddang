package com.ddang.ddang.chat.presentation;

import com.ddang.ddang.chat.application.ChatRoomService;
import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.application.dto.CreateChatRoomDto;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.application.dto.ReadChatRoomWithLastMessageDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.presentation.auth.AuthenticateUser;
import com.ddang.ddang.chat.presentation.auth.AuthenticateUserInfo;
import com.ddang.ddang.chat.presentation.dto.request.CreateChatRoomRequest;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.chat.presentation.dto.response.CreateMessageResponse;
import com.ddang.ddang.chat.presentation.dto.response.ReadChatRoomResponse;
import com.ddang.ddang.chat.presentation.dto.response.ReadChatRoomWithLastMessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/chattings")
@RequiredArgsConstructor
public class ChatRoomController {

    private static final String AUCTIONS_IMAGE_BASE_URL = "/auctions/images/";

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Void> create(
            @AuthenticateUser final AuthenticateUserInfo userInfo,
            @RequestBody @Valid final CreateChatRoomRequest chatRoomRequest
    ) {
        final Long chatRoomId = chatRoomService.create(userInfo.id(), CreateChatRoomDto.from(chatRoomRequest));

        return ResponseEntity.created(URI.create("/chattings/" + chatRoomId))
                             .build();
    }

    @GetMapping
    public ResponseEntity<List<ReadChatRoomWithLastMessageResponse>> readAllParticipatingChatRooms(
            @AuthenticateUser final AuthenticateUserInfo userInfo
    ) {
        final List<ReadChatRoomWithLastMessageDto> readParticipatingChatRoomDtos =
                chatRoomService.readAllByUserId(userInfo.id());

        final List<ReadChatRoomWithLastMessageResponse> responses =
                readParticipatingChatRoomDtos.stream()
                                             .map(dto -> ReadChatRoomWithLastMessageResponse.of(dto, calculateBaseImageUrl()))
                                             .toList();

        return ResponseEntity.ok(responses);
    }

    private String calculateBaseImageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .build()
                                          .toUriString()
                                          .concat(AUCTIONS_IMAGE_BASE_URL);
    }

    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ReadChatRoomResponse> readChatRoomById(
            @PathVariable final Long chatRoomId,
            @AuthenticateUser final AuthenticateUserInfo userInfo
    ) {
        final ReadParticipatingChatRoomDto chatRoomDto = chatRoomService.readByChatRoomId(chatRoomId, userInfo.id());
        final ReadChatRoomResponse response = ReadChatRoomResponse.of(chatRoomDto, calculateBaseImageUrl());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{chatRoomId}/messages")
    public ResponseEntity<CreateMessageResponse> createMessage(
            @PathVariable final Long chatRoomId,
            @RequestBody @Valid final CreateMessageRequest request
    ) {
        final Long messageId = messageService.create(CreateMessageDto.of(chatRoomId, request));
        final CreateMessageResponse response = new CreateMessageResponse(messageId);

        return ResponseEntity.created(URI.create("/chattings/" + chatRoomId + "/messages/" + messageId))
                             .body(response);
    }
}
