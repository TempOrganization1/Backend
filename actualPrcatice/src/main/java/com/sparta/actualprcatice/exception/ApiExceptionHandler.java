package com.sparta.actualprcatice.exception;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseEntity<?> apiNullPointerExceptionHandler(NullPointerException e) {

        ApiException apiException = new ApiException();
        apiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        apiException.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UsernameNotFoundException.class})
    public ResponseEntity<?> apiUsernameNotFoundExceptionHandler(UsernameNotFoundException e) {

        ApiException apiException = new ApiException();
        apiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        apiException.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {SizeLimitExceededException.class})
    public ResponseEntity<?> apiSizeLimitExceededExceptionHandler(SizeLimitExceededException e) {

        ApiException apiException = new ApiException();
        apiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        apiException.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<?> apiIllegalArgumentExceptionHandler(RuntimeException e) {

        ApiException apiException = new ApiException();
        apiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        apiException.setErrorMessage(e.getMessage());

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

}
