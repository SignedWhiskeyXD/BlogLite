package com.wsmrxd.bloglite.exception.handler;

import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private void logRequestError(ErrorCode errorCode, HttpServletRequest request){
        logger.error("Caught Exception: {}({}), Request Method: {}, URI: {}",
                errorCode.name(), errorCode.getErrorCode(),
                request.getMethod(), request.getRequestURI());
    }

    @ExceptionHandler(BlogException.class)
    public RestResponse handleSQLException(HttpServletRequest request, BlogException exception){
        logRequestError(exception.getCode(), request);
        return RestResponse.build(exception.getCode().getErrorCode(), exception.what());
    }
}
