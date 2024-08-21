package devocean.tickit.global.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 존재하지 않는 요청
    @ExceptionHandler(value = {NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public ApiResponse<?> handleNoPageFoundException(Exception e) {
        log.error("GloablExceptionHandler caught NoHandlerFoudException : {}", e.getMessage());
        return ApiResponse.failed(ErrorCode.NOT_FOUND_END_POINT);
    }

    // customed error
    @ExceptionHandler(value = {CustomException.class})
    public ApiResponse<?> handleCustomException(CustomException e) {
        log.error("handleCustomException() in GlobalExceptionHandler threw CustomException : {}", e.getMessage());
        return ApiResponse.failed(e.getErrorCode());
    }

    // default error
    @ExceptionHandler(value = {Exception.class})
    public ApiResponse<?> handleException(Exception e) {
        log.error("handleException() in GlobalExceptionHandler threw Exception : {}", e.getMessage());
        e.printStackTrace();
        return ApiResponse.failed(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
