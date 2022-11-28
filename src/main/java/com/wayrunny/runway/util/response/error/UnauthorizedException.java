package com.wayrunny.runway.util.response.error;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(ErrorResponseStatus status){
        super(status.getMessage());
    }
}
