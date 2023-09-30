package com.ddang.ddang.configuration;

import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.presentation.AuctionController;
import com.ddang.ddang.authentication.application.AuthenticationService;
import com.ddang.ddang.authentication.application.AuthenticationUserService;
import com.ddang.ddang.authentication.application.BlackListTokenService;
import com.ddang.ddang.authentication.presentation.AuthenticationController;
import com.ddang.ddang.bid.application.BidService;
import com.ddang.ddang.bid.presentation.BidController;
import com.ddang.ddang.category.application.CategoryService;
import com.ddang.ddang.category.presentation.CategoryController;
import com.ddang.ddang.chat.application.ChatRoomService;
import com.ddang.ddang.chat.application.MessageService;
import com.ddang.ddang.chat.presentation.ChatRoomController;
import com.ddang.ddang.device.application.DeviceTokenService;
import com.ddang.ddang.device.presentation.DeviceTokenController;
import com.ddang.ddang.image.application.ImageService;
import com.ddang.ddang.image.presentation.ImageController;
import com.ddang.ddang.region.application.RegionService;
import com.ddang.ddang.region.presentation.RegionController;
import com.ddang.ddang.report.application.AuctionReportService;
import com.ddang.ddang.report.application.ChatRoomReportService;
import com.ddang.ddang.report.presentation.ReportController;
import com.ddang.ddang.review.application.ReviewService;
import com.ddang.ddang.review.presentation.ReviewController;
import com.ddang.ddang.user.application.UserService;
import com.ddang.ddang.user.presentation.UserAuctionController;
import com.ddang.ddang.user.presentation.UserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(
        controllers = {
                AuctionController.class,
                AuthenticationController.class,
                BidController.class,
                CategoryController.class,
                ChatRoomController.class,
                DeviceTokenController.class,
                ImageController.class,
                RegionController.class,
                ReportController.class,
                UserAuctionController.class,
                UserController.class,
                ReviewController.class
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
    protected ObjectMapper objectMapper;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected RestDocumentationContextProvider provider;

    @Autowired
    protected AuctionController auctionController;

    @Autowired
    protected AuthenticationController authenticationController;

    @Autowired
    protected CategoryController categoryController;

    @Autowired
    protected BidController bidController;

    @Autowired
    protected ChatRoomController chatRoomController;

    @Autowired
    protected DeviceTokenController deviceTokenController;

    @Autowired
    protected ImageController imageController;

    @Autowired
    protected RegionController regionController;

    @Autowired
    protected ReportController reportController;

    @Autowired
    protected UserAuctionController userAuctionController;

    @Autowired
    protected UserController userController;

    @Autowired
    protected ReviewController reviewController;

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
    protected UserService userService;

    @MockBean
    protected ReviewService reviewService;
}
