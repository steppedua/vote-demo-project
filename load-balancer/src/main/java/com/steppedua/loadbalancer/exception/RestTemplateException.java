package com.steppedua.loadbalancer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RestTemplateException extends RuntimeException {
    public RestTemplateException(String message, Throwable cause) {
        super(message, cause);
    }
}
