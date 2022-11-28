package com.wayrunny.runway.util.response.error;

public class ConflictException extends RuntimeException{
    public ConflictException(ErrorResponseStatus status){
        super(status.getMessage());
    }
}
