package com.wayrunny.runway.util.response;

import com.wayrunny.runway.util.response.error.ErrorResponseStatus;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseApiException {

    private Integer status;
    private String message;

    public BaseApiException(ErrorResponseStatus status){
        this.status = status.getCode().value();
        this.message = status.getMessage();
    }

    public BaseApiException(HttpStatus code, String message){
        this.status = code.value();
        this.message = message;
    }

}