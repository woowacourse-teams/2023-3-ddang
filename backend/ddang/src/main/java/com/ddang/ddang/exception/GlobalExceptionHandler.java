package com.ddang.ddang.exception;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.application.exception.UserForbiddenException;
import com.ddang.ddang.auction.domain.exception.InvalidPriceValueException;
import com.ddang.ddang.auction.domain.exception.WinnerNotFoundException;
import com.ddang.ddang.authentication.configuration.exception.UserUnauthorizedException;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import com.ddang.ddang.authentication.domain.exception.UnsupportedSocialLoginException;
import com.ddang.ddang.bid.application.exception.InvalidBidException;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.chat.application.exception.ChatRoomNotFoundException;
import com.ddang.ddang.chat.application.exception.InvalidAuctionToChatException;
import com.ddang.ddang.chat.application.exception.MessageNotFoundException;
import com.ddang.ddang.chat.application.exception.UnableToChatException;
import com.ddang.ddang.chat.application.exception.UserNotAccessibleException;
import com.ddang.ddang.device.application.exception.DeviceTokenNotFoundException;
import com.ddang.ddang.exception.dto.ExceptionResponse;
import com.ddang.ddang.image.application.exception.ImageNotFoundException;
import com.ddang.ddang.image.infrastructure.local.exception.EmptyImageException;
import com.ddang.ddang.image.infrastructure.local.exception.StoreImageFailureException;
import com.ddang.ddang.image.infrastructure.local.exception.UnsupportedImageFileExtensionException;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.report.application.exception.AlreadyReportAuctionException;
import com.ddang.ddang.report.application.exception.AlreadyReportChatRoomException;
import com.ddang.ddang.report.application.exception.ChatRoomReportNotAccessibleException;
import com.ddang.ddang.report.application.exception.InvalidReportAuctionException;
import com.ddang.ddang.report.application.exception.InvalidReporterToAuctionException;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.MalformedURLException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String EXCEPTION_FORMAT = "%s : ";

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            final Exception ex,
            final Object body,
            final HttpHeaders headers,
            final HttpStatusCode statusCode,
            final WebRequest request
    ) {
        logger.error(String.format(EXCEPTION_FORMAT, ex.getClass()
                                                       .getSimpleName()), ex);

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCategoryNotFoundException(final CategoryNotFoundException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, CategoryNotFoundException.class), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(RegionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleRegionNotFoundException(final RegionNotFoundException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, RegionNotFoundException.class), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleChatRoomNotFoundException(final ChatRoomNotFoundException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, ChatRoomNotFoundException.class), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(MessageNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleMessageNotFoundException(final MessageNotFoundException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, MessageNotFoundException.class), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(UnableToChatException.class)
    public ResponseEntity<ExceptionResponse> handleUnableToChatException(final UnableToChatException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, MessageNotFoundException.class), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(final UserNotFoundException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, UserNotFoundException.class), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(AuctionNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleAuctionNotFoundException(final AuctionNotFoundException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, AuctionNotFoundException.class), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidBidException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidBidException(final InvalidBidException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, InvalidBidException.class), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotAuthorizationException(
            final UserForbiddenException ex
    ) {
        logger.warn(String.format(EXCEPTION_FORMAT, UserForbiddenException.class), ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidPriceValueException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPriceValueException(
            final InvalidPriceValueException ex
    ) {
        logger.warn(String.format(EXCEPTION_FORMAT, InvalidPriceValueException.class), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(EmptyImageException.class)
    public ResponseEntity<ExceptionResponse> handleEmptyImageException(final EmptyImageException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, EmptyImageException.class), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(StoreImageFailureException.class)
    public ResponseEntity<ExceptionResponse> handleStoreImageFailureException(
            final StoreImageFailureException ex
    ) {
        logger.error(String.format(EXCEPTION_FORMAT, StoreImageFailureException.class), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(UnsupportedImageFileExtensionException.class)
    public ResponseEntity<ExceptionResponse> handleUnsupportedImageFileExtensionException(
            final UnsupportedImageFileExtensionException ex
    ) {
        logger.warn(String.format(EXCEPTION_FORMAT, UnsupportedImageFileExtensionException.class), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleImageNotFoundException(final ImageNotFoundException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, ImageNotFoundException.class), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<ExceptionResponse> handleMalformedURLException(final MalformedURLException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, MalformedURLException.class), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ExceptionResponse("이미지 조회에 실패했습니다."));
    }

    @ExceptionHandler(UserNotAccessibleException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotAccessibleException(
            final UserNotAccessibleException ex
    ) {
        logger.warn(String.format(EXCEPTION_FORMAT, UserNotAccessibleException.class), ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(WinnerNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleWinnerNotFoundException(
            final WinnerNotFoundException ex
    ) {
        logger.warn(String.format(EXCEPTION_FORMAT, WinnerNotFoundException.class), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidAuctionToChatException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidAuctionToChatException(
            final InvalidAuctionToChatException ex
    ) {
        logger.warn(String.format(EXCEPTION_FORMAT, InvalidAuctionToChatException.class), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidReporterToAuctionException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidReporterToAuctionException(final InvalidReporterToAuctionException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, InvalidReporterToAuctionException.class), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidReportAuctionException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidReportAuctionException(final InvalidReportAuctionException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, InvalidReportAuctionException.class), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(AlreadyReportAuctionException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyReportAuctionException(final AlreadyReportAuctionException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, AlreadyReportAuctionException.class), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(ChatRoomReportNotAccessibleException.class)
    public ResponseEntity<ExceptionResponse> handleChatRoomReportNotAccessibleExceptionException(final ChatRoomReportNotAccessibleException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, ChatRoomReportNotAccessibleException.class), ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(AlreadyReportChatRoomException.class)
    public ResponseEntity<ExceptionResponse> handleAlreadyReportChatRoomException(final AlreadyReportChatRoomException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, AlreadyReportChatRoomException.class), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidTokenException(final InvalidTokenException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, InvalidTokenException.class), ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(UnsupportedSocialLoginException.class)
    public ResponseEntity<ExceptionResponse> handleUnsupportedSocialLoginException(
            final UnsupportedSocialLoginException ex
    ) {
        logger.warn(String.format(EXCEPTION_FORMAT, UnsupportedSocialLoginException.class), ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(UserUnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUserUnauthorizedException(final UserUnauthorizedException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, UserUnauthorizedException.class), ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @ExceptionHandler(DeviceTokenNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleDeviceTokenNotFoundException(final DeviceTokenNotFoundException ex) {
        logger.warn(String.format(EXCEPTION_FORMAT, DeviceTokenNotFoundException.class), ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(new ExceptionResponse(ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request
    ) {
        logger.info(String.format(EXCEPTION_FORMAT, MethodArgumentNotValidException.class), ex);

        final String message = ex.getFieldErrors()
                                 .stream()
                                 .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                 .collect(Collectors.joining(System.lineSeparator()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ExceptionResponse(message));
    }
}
