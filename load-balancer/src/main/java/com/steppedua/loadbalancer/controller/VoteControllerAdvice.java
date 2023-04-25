package com.steppedua.loadbalancer.controller;

import com.steppedua.loadbalancer.exception.RestTemplateException;
import com.steppedua.loadbalancer.model.ErrorDto;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class VoteControllerAdvice {

    @ResponseBody
    @ExceptionHandler(value = {RestTemplateException.class})
    //TODO поставить правильный статус ответа
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleException(@NonNull final RestTemplateException exception) {
        log.error(exception.getMessage(), exception);
        return ErrorDto.builder().code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(exception.getMessage()).build();
    }
}
