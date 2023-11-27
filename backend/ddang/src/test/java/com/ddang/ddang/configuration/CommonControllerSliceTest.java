package com.ddang.ddang.configuration;

import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.presentation.AuctionReviewController;
import com.ddang.ddang.authentication.application.AuthenticationService;
import com.ddang.ddang.authentication.application.AuthenticationUserService;
import com.ddang.ddang.authentication.application.BlackListTokenService;
import com.ddang.ddang.authentication.application.SocialUserInformationService;
import com.ddang.ddang.authentication.presentation.AuthenticationController;
import com.ddang.ddang.bid.application.BidService;
import com.ddang.ddang.category.application.CategoryService;
import com.ddang.ddang.category.presentation.CategoryController;
import com.ddang.ddang.chat.application.ChatRoomService;
import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.device.application.DeviceTokenService;
import com.ddang.ddang.device.presentation.DeviceTokenController;
import com.ddang.ddang.image.application.ImageService;
import com.ddang.ddang.image.configuration.ImageRelativeUrlConfigurationProperties;
import com.ddang.ddang.image.presentation.ImageController;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrlFinder;
import com.ddang.ddang.qna.application.AnswerService;
import com.ddang.ddang.qna.application.QuestionService;
import com.ddang.ddang.region.application.RegionService;
import com.ddang.ddang.region.presentation.RegionController;
import com.ddang.ddang.report.application.AnswerReportService;
import com.ddang.ddang.report.application.AuctionReportService;
import com.ddang.ddang.report.application.ChatRoomReportService;
import com.ddang.ddang.report.application.QuestionReportService;
import com.ddang.ddang.report.presentation.ReportController;
import com.ddang.ddang.review.application.ReviewService;
import com.ddang.ddang.user.application.UserService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(
        controllers = {
                AuthenticationController.class,
                CategoryController.class,
                DeviceTokenController.class,
                ImageController.class,
                RegionController.class,
                ReportController.class,
                AuctionReviewController.class
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.ddang\\.ddang\\.authentication\\.configuration\\..*")
        }
)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public abstract class CommonControllerSliceTest {

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected RestDocumentationContextProvider provider;

    @Autowired
    protected AuthenticationController authenticationController;

    @Autowired
    protected CategoryController categoryController;

    @Autowired
    protected DeviceTokenController deviceTokenController;

    @Autowired
    protected ImageController imageController;

    @Autowired
    protected RegionController regionController;

    @Autowired
    protected ReportController reportController;

    @Autowired
    protected AuctionReviewController auctionReviewController;

    @MockBean
    protected AuctionService auctionService;

    @MockBean
    protected ChatRoomService chatRoomService;

    @MockBean
    protected BlackListTokenService blackListTokenService;

    @MockBean
    protected AuthenticationUserService authenticationUserService;

    @MockBean
    protected AuthenticationService authenticationService;

    @MockBean
    protected BidService bidService;

    @MockBean
    protected CategoryService categoryService;

    @MockBean
    protected MessageService messageService;

    @MockBean
    protected DeviceTokenService deviceTokenService;

    @MockBean
    protected ImageService imageService;

    @MockBean
    protected RegionService regionService;

    @MockBean
    protected AuctionReportService auctionReportService;

    @MockBean
    protected ChatRoomReportService chatRoomReportService;

    @MockBean
    protected QuestionReportService questionReportService;

    @MockBean
    protected AnswerReportService answerReportService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected QuestionService questionService;

    @MockBean
    protected AnswerService answerService;

    @MockBean
    protected ReviewService reviewService;

    @MockBean
    protected SocialUserInformationService socialUserInformationService;

    protected ImageRelativeUrlConfigurationProperties imageRelativeUrl =
            new ImageRelativeUrlConfigurationProperties("/auctions/images/", "/users/images/");

    protected ImageRelativeUrlFinder urlFinder = new ImageRelativeUrlFinder(imageRelativeUrl);

    protected ObjectMapper objectMapper;

    protected MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @BeforeEach
    void controllerSliceTestSetUp() {
        objectMapper = new ObjectMapper();

        final SimpleModule module = new SimpleModule();

        module.addSerializer(LocalDateTime.class, new JsonSerializer<>() {
            @Override
            public void serialize(final LocalDateTime value, final JsonGenerator gen,
                    final SerializerProvider serializers)
                    throws IOException {
                final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

                gen.writeString(formatter.format(value));
            }
        });

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(module);
        mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
    }
}
