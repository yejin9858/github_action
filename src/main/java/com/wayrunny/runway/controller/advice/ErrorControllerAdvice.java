package com.wayrunny.runway.controller.advice;

import com.wayrunny.runway.util.response.BaseApiException;
import com.wayrunny.runway.util.response.error.BadRequestException;
import com.wayrunny.runway.util.response.error.ConflictException;
import com.wayrunny.runway.util.response.error.NotFoundException;
import com.wayrunny.runway.util.response.error.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.naming.SizeLimitExceededException;


@ControllerAdvice
public class ErrorControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequest(final BadRequestException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        BaseApiException apiException = new BaseApiException(httpStatus, e.getMessage());
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<Object> handleUnauthorized(final UnauthorizedException e) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        BaseApiException apiException = new BaseApiException(httpStatus, e.getMessage());
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFound(final NotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        BaseApiException apiException = new BaseApiException(httpStatus, e.getMessage());
        return new ResponseEntity<>(apiException, httpStatus);
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflict(final ConflictException e) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        BaseApiException apiException = new BaseApiException(httpStatus, e.getMessage());
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SizeLimitExceededException.class)
    protected ResponseEntity<Object> handleSizeExceed(final ConflictException e) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        BaseApiException apiException = new BaseApiException(httpStatus, "5MB이하의 이미지만 등록할 수 있습니다.");
        return new ResponseEntity<>(apiException, httpStatus);
    }

}
