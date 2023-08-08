package com.ddang.ddang.chat.presentation;

import com.ddang.ddang.chat.application.ChatRoomService;
import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.application.dto.CreateMessageDto;
import com.ddang.ddang.chat.application.dto.ReadMessageDto;
import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.chat.presentation.auth.AuthenticateUser;
import com.ddang.ddang.chat.presentation.auth.AuthenticateUserInfo;
import com.ddang.ddang.chat.presentation.dto.request.CreateMessageRequest;
import com.ddang.ddang.chat.presentation.dto.request.ReadMessageRequest;
import com.ddang.ddang.chat.presentation.dto.response.CreateMessageResponse;
import com.ddang.ddang.chat.presentation.dto.response.ReadChatRoomResponse;
import com.ddang.ddang.chat.presentation.dto.response.ReadChatRoomsResponse;
import com.ddang.ddang.chat.presentation.dto.response.ReadMessageResponse;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/chattings")
@RequiredArgsConstructor
public class ChatRoomController {

    private static final String AUCTIONS_IMAGE_BASE_URL = "/auctions/images/";

    private final ChatRoomService chatRoomService;
    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<ReadChatRoomsResponse> readAllParticipatingChatRooms(
            @AuthenticateUser final AuthenticateUserInfo userInfo
    ) {
        final List<ReadParticipatingChatRoomDto> readParticipatingChatRoomDtos =
                chatRoomService.readAllByUserId(userInfo.id());

        final List<ReadChatRoomResponse> responses =
                readParticipatingChatRoomDtos.stream()
                                             .map(dto -> ReadChatRoomResponse.of(dto, calculateBaseImageUrl()))
                                             .toList();
        final ReadChatRoomsResponse response = new ReadChatRoomsResponse(responses);

        return ResponseEntity.ok(response);
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
            @AuthenticateUser final AuthenticateUserInfo userInfo,
            @PathVariable final Long chatRoomId,
            @RequestBody @Valid final CreateMessageRequest request
    ) {
        final Long messageId = messageService.create(CreateMessageDto.of(userInfo.id(), chatRoomId, request));
        final CreateMessageResponse response = new CreateMessageResponse(messageId);

        return ResponseEntity.created(URI.create("/chattings/" + chatRoomId + "/messages/" + messageId))
                             .body(response);
    }

    @GetMapping("/{chatRoomId}/messages/{lastMessageId}")
    public ResponseEntity<List<ReadMessageResponse>> readAllByLastMessageId(
            @AuthenticateUser final AuthenticateUserInfo userInfo,
            @PathVariable final Long chatRoomId,
            @PathVariable(required = false) final Long lastMessageId
    ) {
        final ReadMessageRequest readMessageRequest = new ReadMessageRequest(userInfo.id(), chatRoomId, lastMessageId);
        final List<ReadMessageDto> readMessageDtos = messageService.readAllByLastMessageId(readMessageRequest);
        final List<ReadMessageResponse> responses = readMessageDtos.stream()
                                                                   .map(readMessageDto -> ReadMessageResponse.of(
                                                                           readMessageDto,
                                                                           calculateMessageOwner(readMessageDto, userInfo))
                                                                   )
                                                                   .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    private boolean calculateMessageOwner(final ReadMessageDto readMessageDto, final AuthenticateUserInfo userInfo) {
        return readMessageDto.writer()
                             .getId()
                             .equals(userInfo.id());
    }
}
