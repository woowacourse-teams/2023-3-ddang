package com.ddang.ddang.review.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticateUser;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.review.application.ReviewService;
import com.ddang.ddang.review.application.dto.CreateReviewDto;
import com.ddang.ddang.review.application.dto.ReadReviewDto;
import com.ddang.ddang.review.presentation.request.CreateReviewRequest;
import com.ddang.ddang.review.presentation.response.ReadReviewResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Void> create(
            @AuthenticateUser final AuthenticationUserInfo userInfo,
            @RequestBody @Valid final CreateReviewRequest reviewRequest
    ) {
        final Long reviewId = reviewService.create(CreateReviewDto.of(userInfo.userId(), reviewRequest));

        return ResponseEntity.created(URI.create("/reviews/" + reviewId))
                             .build();
    }

    @GetMapping
    public ResponseEntity<List<ReadReviewResponse>> read(@RequestParam final Long userId) {
        final List<ReadReviewDto> readReviewDtos = reviewService.readAllByTargetId(userId);
        final List<ReadReviewResponse> response = readReviewDtos.stream()
                                                                .map(ReadReviewResponse::from)
                                                                .toList();

        return ResponseEntity.ok(response);
    }
}
