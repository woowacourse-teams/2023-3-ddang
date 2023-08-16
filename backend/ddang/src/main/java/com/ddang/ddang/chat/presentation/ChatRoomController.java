package com.ddang.ddang.chat.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.chat.application.ChatRoomService;
import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.application.dto.CreateChatRoomDto;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.application.dto.ReadChatRoomWithLastMessageDto;
import com.ddang.ddang.chat.application.dto.ReadMessageDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.presentation.dto.request.CreateChatRoomRequest;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.chat.presentation.dto.request.ReadMessageRequest;
import com.ddang.ddang.chat.presentation.dto.response.CreateChatRoomResponse;
import com.ddang.ddang.chat.presentation.dto.response.CreateMessageResponse;
import com.ddang.ddang.chat.presentation.dto.response.ReadChatRoomResponse;
import com.ddang.ddang.chat.presentation.dto.response.ReadChatRoomWithLastMessageResponse;
import com.ddang.ddang.chat.presentation.dto.response.ReadMessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<CreateChatRoomResponse> create(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CreateChatRoomRequest chatRoomRequest
    ) {
        final Long chatRoomId = chatRoomService.create(userInfo.userId(), CreateChatRoomDto.from(chatRoomRequest));
        final CreateChatRoomResponse response = new CreateChatRoomResponse(chatRoomId);

        return ResponseEntity.created(URI.create("/chattings/" + chatRoomId))
                             .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReadChatRoomWithLastMessageResponse>> readAllParticipatingChatRooms(
            @AuthenticateUser final AuthenticationUserInfo userInfo
    ) {
        final List<ReadChatRoomWithLastMessageDto> readParticipatingChatRoomDtos =
                chatRoomService.readAllByUserId(userInfo.userId());

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
            @AuthenticateUser final AuthenticationUserInfo userInfo
    ) {
        final ReadParticipatingChatRoomDto chatRoomDto = chatRoomService.readByChatRoomId(chatRoomId, userInfo.userId());
        final ReadChatRoomResponse response = ReadChatRoomResponse.of(chatRoomDto, calculateBaseImageUrl());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{chatRoomId}/messages")
    public ResponseEntity<CreateMessageResponse> createMessage(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @PathVariable final Long chatRoomId,
            @RequestBody @Valid final CreateMessageRequest request
    ) {
        final Long messageId = messageService.create(CreateMessageDto.of(userInfo.userId(), chatRoomId, request));
        final CreateMessageResponse response = new CreateMessageResponse(messageId);

        return ResponseEntity.created(URI.create("/chattings/" + chatRoomId))
                             .body(response);
    }

    @GetMapping("/{chatRoomId}/messages")
    public ResponseEntity<List<ReadMessageResponse>> readAllByLastMessageId(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @PathVariable final Long chatRoomId,
            @RequestParam(required = false) final Long lastMessageId
    ) {
        final ReadMessageRequest readMessageRequest = new ReadMessageRequest(
                userInfo.userId(),
                chatRoomId,
                lastMessageId
        );
        final List<ReadMessageDto> readMessageDtos = messageService.readAllByLastMessageId(readMessageRequest);
        final List<ReadMessageResponse> responses = readMessageDtos.stream()
                                                                   .map(readMessageDto -> ReadMessageResponse.of(
                                                                           readMessageDto,
                                                                           isMessageOwner(
                                                                                   readMessageDto,
                                                                                   userInfo
                                                                           )
                                                                   ))
                                                                   .toList();
        return ResponseEntity.ok(responses);
    }

    private boolean isMessageOwner(final ReadMessageDto readMessageDto, final AuthenticationUserInfo userInfo) {
        return readMessageDto.writerDto()
                             .id()
                             .equals(userInfo.userId());
    }
}
