package com.ddang.ddang.chat.application.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.chat.application.event.CreateReadMessageLogEvent;
import com.ddang.ddang.chat.application.event.UpdateReadMessageLogEvent;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.BeforeEach;

@SuppressWarnings("NonAsciiCharacters")
public class LastReadMessageLogEventListenerFixture {

    protected CreateReadMessageLogEvent 생성용_메시지_조회_로그;
    protected UpdateReadMessageLogEvent 업데이트용_메시지_조회_로그;
    protected User 메시지_로그_생성용_발신자_겸_판매자;
    protected User 메시지_로그_생성용_입찰자_구매자;
    protected User 메시지_로그_업데이트용_발신자_겸_판매자;
    protected User 메시지_로그_업데이트용_입찰자;
    protected ChatRoom 메시지_로그_생성용_채팅방;
    protected Auction 메시지_로그_생성용_경매;

    @BeforeEach
    void setUp() {
        메시지_로그_생성용_발신자_겸_판매자 = User.builder()
                                   .name("메시지_로그_생성용_발신자")
                                   .profileImage(new ProfileImage("upload.png", "store.png"))
                                   .reliability(new Reliability(4.7d))
                                   .oauthId("12345")
                                   .build();
        메시지_로그_생성용_입찰자_구매자 = User.builder()
                                 .name("메시지_로그_생성용_입찰자")
                                 .profileImage(new ProfileImage("upload.png", "store.png"))
                                 .reliability(new Reliability(4.7d))
                                 .oauthId("12346")
                                 .build();
        메시지_로그_업데이트용_발신자_겸_판매자 = User.builder()
                                     .name("메시지_로그_업데이트용_발신자")
                                     .profileImage(new ProfileImage("upload.png", "store.png"))
                                     .reliability(new Reliability(4.7d))
                                     .oauthId("12347")
                                     .build();
        메시지_로그_업데이트용_입찰자 = User.builder()
                               .name("메시지_로그_업데이트용_입찰자")
                               .profileImage(new ProfileImage("upload.png", "store.png"))
                               .reliability(new Reliability(4.7d))
                               .oauthId("12348")
                               .build();

        메시지_로그_생성용_채팅방 = new ChatRoom(메시지_로그_생성용_경매, 메시지_로그_생성용_입찰자_구매자);

        final Message 메시지 = Message.builder()
                                   .chatRoom(메시지_로그_생성용_채팅방)
                                   .writer(메시지_로그_생성용_발신자_겸_판매자)
                                   .contents("메시지")
                                   .build();

        업데이트용_메시지_조회_로그 = new UpdateReadMessageLogEvent(메시지_로그_생성용_발신자_겸_판매자, 메시지_로그_생성용_채팅방, 메시지);
        생성용_메시지_조회_로그 = new CreateReadMessageLogEvent(메시지_로그_생성용_채팅방);
    }
}
