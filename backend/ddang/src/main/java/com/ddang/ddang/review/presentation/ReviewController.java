package com.ddang.ddang.review.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrlFinder;
import com.ddang.ddang.image.presentation.util.ImageTargetType;
import com.ddang.ddang.review.application.ReviewService;
import com.ddang.ddang.review.application.dto.request.CreateReviewDto;
import com.ddang.ddang.review.application.dto.response.ReadReviewDetailDto;
import com.ddang.ddang.review.application.dto.response.ReadReviewDto;
import com.ddang.ddang.review.presentation.dto.request.CreateReviewRequest;
import com.ddang.ddang.review.presentation.dto.response.ReadReviewDetailResponse;
import com.ddang.ddang.review.presentation.dto.response.ReadReviewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ImageRelativeUrlFinder urlFinder;

    @PostMapping
    public ResponseEntity<Void> create(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CreateReviewRequest reviewRequest
    ) {
        final Long reviewId = reviewService.create(CreateReviewDto.of(userInfo.userId(), reviewRequest));

        return ResponseEntity.created(URI.create("/reviews/" + reviewId))
                             .build();
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReadReviewDetailResponse> read(@PathVariable final Long reviewId) {
        final ReadReviewDetailDto readReviewDetailDto = reviewService.readByReviewId(reviewId);
        ReadReviewDetailResponse response = ReadReviewDetailResponse.from(readReviewDetailDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ReadReviewResponse>> readAllReviewsOfTargetUser(@PathVariable final Long userId) {
        final List<ReadReviewDto> readReviewDtos = reviewService.readAllByTargetId(userId);
        final List<ReadReviewResponse> response = readReviewDtos.stream()
                                                                .map(dto -> ReadReviewResponse.of(
                                                                        dto,
                                                                        urlFinder.find(ImageTargetType.PROFILE_IMAGE)
                                                                ))
                                                                .toList();

        return ResponseEntity.ok(response);
    }
}
