package com.ddang.ddang.image.presentation;

import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.image.application.exception.ImageNotFoundException;
import com.ddang.ddang.image.presentation.fixture.ProfileImageControllerFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.MalformedURLException;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
class ProfileImageControllerTest extends ProfileImageControllerFixture {

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    void 지정한_사용자_아이디에_대한_사용자_이미지를_조회한다() throws Exception {
        // given
        given(imageService.readProfileImage(anyLong())).willReturn(이미지_파일_리소스);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/users/images/{id}", 프로필_이미지_아이디))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.IMAGE_JPEG))
               .andExpect(content().bytes(이미지_파일_바이트));
    }

    @Test
    void 사용자_이미지_조회시_지정한_아이디에_대한_이미지가_없는_경우_404를_반환한다() throws Exception {
        // given
        given(imageService.readProfileImage(anyLong())).willThrow(new ImageNotFoundException("지정한 이미지를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/users/images/{id}", 존재하지_않는_프로필_이미지_아이디))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 사용자_이미지_조회시_유효한_프로토콜이나_URL이_아닌_경우_500을_반환한다() throws Exception {
        // given
        given(imageService.readProfileImage(anyLong())).willThrow(new MalformedURLException());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/users/images/{id}", 프로필_이미지_아이디))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 지정한_아이디에_대한_경매_이미지를_조회한다() throws Exception {
        // given
        given(imageService.readAuctionImage(anyLong())).willReturn(이미지_파일_리소스);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/auctions/images/{id}", 경매_이미지_아이디))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.IMAGE_JPEG))
               .andExpect(content().bytes(이미지_파일_바이트));
    }

    @Test
    void 경매_이미지_조회시_지정한_아이디에_대한_이미지가_없는_경우_404를_반환한다() throws Exception {
        // given
        given(imageService.readAuctionImage(anyLong())).willThrow(new ImageNotFoundException("지정한 이미지를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/auctions/images/{id}", 존재하지_않는_경매_이미지_아이디))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 경매_이미지_조회시_유효한_프로토콜이나_URL이_아닌_경우_500을_반환한다() throws Exception {
        // given
        given(imageService.readAuctionImage(anyLong())).willThrow(new MalformedURLException());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/auctions/images/{id}", 경매_이미지_아이디))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.message").exists());
    }
}
