package com.ddang.ddang.image.presentation;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.image.application.ImageService;
import com.ddang.ddang.image.application.exception.ImageNotFoundException;
import java.net.MalformedURLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(controllers = {ImageController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.ddang\\.ddang\\.authentication\\.configuration\\..*")
        }
)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProfileImageControllerTest {

    @MockBean
    ImageService imageService;

    @Autowired
    ImageController imageController;

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
        final byte[] imageBytes = "이것은 이미지 파일의 바이트 코드입니다.".getBytes();
        final Resource mockResource = new ByteArrayResource(imageBytes);

        given(imageService.readImage(anyLong())).willReturn(mockResource);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/users/images/{id}", 1L))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.IMAGE_PNG))
               .andExpect(content().bytes(imageBytes));
    }

    @Test
    void 사용자_이미지_조회시_지정한_아이디에_대한_이미지가_없는_경우_404를_반환한다() throws Exception {
        // given
        final Long invalidAuctionImageId = 1L;

        given(imageService.readImage(anyLong())).willThrow(new ImageNotFoundException(
                "지정한 이미지를 찾을 수 없습니다."
        ));

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/users/images/{id}", invalidAuctionImageId))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 사용자_이미지_조회시_유효한_프로토콜이나_URL이_아닌_경우_500을_반환한다() throws Exception {
        // given
        final Long invalidAuctionImageId = 1L;

        given(imageService.readImage(anyLong())).willThrow(new MalformedURLException());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/users/images/{id}", invalidAuctionImageId))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 지정한_아이디에_대한_경매_이미지를_조회한다() throws Exception {
        // given
        final byte[] imageBytes = "이것은 이미지 파일의 바이트 코드입니다.".getBytes();
        final Resource mockResource = new ByteArrayResource(imageBytes);

        given(imageService.readAuctionImage(anyLong())).willReturn(mockResource);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/auctions/images/{id}", 1L))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.IMAGE_PNG))
               .andExpect(content().bytes(imageBytes));
    }

    @Test
    void 경매_이미지_조회시_지정한_아이디에_대한_이미지가_없는_경우_404를_반환한다() throws Exception {
        // given
        final Long invalidAuctionImageId = 1L;

        given(imageService.readAuctionImage(anyLong())).willThrow(new ImageNotFoundException(
                "지정한 이미지를 찾을 수 없습니다."
        ));

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/auctions/images/{id}", invalidAuctionImageId))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 경매_이미지_조회시_유효한_프로토콜이나_URL이_아닌_경우_500을_반환한다() throws Exception {
        // given
        final Long invalidAuctionImageId = 1L;

        given(imageService.readAuctionImage(anyLong())).willThrow(new MalformedURLException());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/auctions/images/{id}", invalidAuctionImageId))
               .andExpect(status().isInternalServerError())
               .andExpect(jsonPath("$.message").exists());
    }
}
