package com.wangkisa.commerce.exception;

import com.wangkisa.commerce.domain.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ApiResponse<?> handleCustomException(CustomException ce, WebRequest request) {
        log.error("call handleCustomException()");

        return ApiResponse.error(ce.getCode(), ce.getMessage(), ce.getErrorDetails());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<?> handlerRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("======================= Handler RuntimeException =======================");
        e.printStackTrace();
        return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), e.getMessage(), Arrays.asList(request.getRequestURI()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ApiResponse<?> handlerHttpUsernameNotFoundException(UsernameNotFoundException e, HttpServletRequest request) {
        log.error("======================= Handler UsernameNotFoundException =======================");
        e.printStackTrace();
        return ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), Arrays.asList(e.getMessage()));
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<?> handlerIllegalArgumentException(IllegalArgumentException e) {
        e.printStackTrace();
        return ApiResponse.error(BAD_REQUEST.value(), e.getMessage(), Arrays.asList(e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();
        return ApiResponse.error(BAD_REQUEST.value(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), Arrays.asList(e.getMessage()));
    }

}
