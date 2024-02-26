package com.wsmrxd.bloglite.exception.handler;

import com.wsmrxd.bloglite.enums.ErrorCode;
import com.wsmrxd.bloglite.exception.BlogAuthException;
import com.wsmrxd.bloglite.exception.BlogException;
import com.wsmrxd.bloglite.vo.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BlogException.class)
    public RestResponse handleSQLException(HttpServletRequest request, BlogException exception){
        logRequestError(exception.getCode(), request);
        return RestResponse.build(exception.getCode().getErrorCode(), exception.what());
    }

    @ExceptionHandler(BlogAuthException.class)
    public String handleBlogAuthException(HttpServletRequest request, HttpServletResponse response,
                                                BlogAuthException exception) {
        logRequestError(401, exception.getMessage(), request);
        response.setStatus(401);
        return exception.getMessage();
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public RestResponse handleSQLNoneUniqueException(HttpServletRequest request,
                                                     SQLIntegrityConstraintViolationException exception){
        logRequestError(exception.getErrorCode(), exception.getMessage(),  request);
        return RestResponse.build(exception.getErrorCode(), exception.getMessage());
    }

    private void logRequestError(ErrorCode errorCode, HttpServletRequest request){
        log.warn("Caught Exception: {}({}), Request Method: {}, URI: {}",
                errorCode.name(), errorCode.getErrorCode(),
                request.getMethod(), request.getRequestURI());
    }

    private void logRequestError(int errorCode, String errorName, HttpServletRequest request){
        log.warn("Caught Exception: {}({}), Request Method: {}, URI: {}",
                errorName, errorCode,
                request.getMethod(), request.getRequestURI());
    }
}
