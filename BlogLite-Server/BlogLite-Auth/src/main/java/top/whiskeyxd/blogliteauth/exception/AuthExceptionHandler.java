package top.whiskeyxd.blogliteauth.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.whiskeyxd.bloglitecommon.enums.ErrorCode;
import top.whiskeyxd.bloglitecommon.exception.BlogLiteException;
import top.whiskeyxd.bloglitecommon.vo.RestResponse;

@RestControllerAdvice
@Slf4j
public class AuthExceptionHandler {

    @ExceptionHandler(BlogLiteException.class)
    public RestResponse<Object> handleBlogLiteException(BlogLiteException exception, HttpServletRequest request){
        ErrorCode code = exception.getErrorCode();
        log.warn("Caught Exception: {}({}), Request Method: {}, URI: {}",
                code.what(), code.getCode(), request.getMethod(), request.getRequestURI());
        return RestResponse.build(code.getCode(), code.what());
    }
}
