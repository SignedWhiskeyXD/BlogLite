package com.wsmrxd.bloglite.exception.handler;

import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.vo.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class ControllerExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private void logRequestError(ErrorCode errorCode, HttpServletRequest request){
        logger.warn("Caught Exception: {}({}), Request Method: {}, URI: {}",
                errorCode.name(), errorCode.getErrorCode(),
                request.getMethod(), request.getRequestURI());
    }

    private void logRequestError(int errorCode, String errorName, HttpServletRequest request){
        logger.warn("Caught Exception: {}({}), Request Method: {}, URI: {}",
                errorName, errorCode,
                request.getMethod(), request.getRequestURI());
    }

    @ExceptionHandler(BlogException.class)
    public RestResponse handleSQLException(HttpServletRequest request, BlogException exception){
        logRequestError(exception.getCode(), request);
        return RestResponse.build(exception.getCode().getErrorCode(), exception.what());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public RestResponse handleSQLNoneUniqueException(HttpServletRequest request,
                                                     SQLIntegrityConstraintViolationException exception){
        logRequestError(exception.getErrorCode(), exception.getMessage(),  request);
        return RestResponse.build(exception.getErrorCode(), exception.getMessage());
    }
}
