package com.ddang.ddang.review.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrlFinder;
import com.ddang.ddang.image.presentation.util.ImageTargetType;
import com.ddang.ddang.review.application.ReviewService;
import com.ddang.ddang.review.application.dto.request.CreateReviewDto;
import com.ddang.ddang.review.application.dto.response.ReadSingleReviewDto;
import com.ddang.ddang.review.application.dto.response.ReadMultipleReviewDto;
import com.ddang.ddang.review.presentation.dto.request.CreateReviewRequest;
import com.ddang.ddang.review.presentation.dto.response.ReadSingleReviewResponse;
import com.ddang.ddang.review.presentation.dto.response.ReadMultipleReviewResponse;
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
    public ResponseEntity<ReadSingleReviewResponse> read(@PathVariable final Long reviewId) {
        final ReadSingleReviewDto readSingleReviewDto = reviewService.readByReviewId(reviewId);
        ReadSingleReviewResponse response = ReadSingleReviewResponse.from(readSingleReviewDto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ReadMultipleReviewResponse>> readAllReviewsOfTargetUser(@PathVariable final Long userId) {
        final List<ReadMultipleReviewDto> readMultipleReviewDtos = reviewService.readAllByTargetId(userId);
        final List<ReadMultipleReviewResponse> response = readMultipleReviewDtos.stream()
                                                                                .map(dto -> ReadMultipleReviewResponse.of(
                                                                        dto,
                                                                        urlFinder.find(ImageTargetType.PROFILE_IMAGE)
                                                                ))
                                                                                .toList();

        return ResponseEntity.ok(response);
    }
}
