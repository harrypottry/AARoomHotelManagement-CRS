package com.aaroom.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice(basePackages = {"com.aaroom.controller"})
public class RestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);


    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public RestError handle401(HttpServletRequest req,
                               HttpServletResponse rep,
                               UnauthorizedException ex) {

        rep.setHeader("WWW-Authenticate", "OAuth realm=\"aaroom.com\"");
        return ex.getRestError();
    }


    @ExceptionHandler(RestException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RestError handle(HttpServletRequest req, RestException ex) {
        return ex.getRestError();
    }


}
