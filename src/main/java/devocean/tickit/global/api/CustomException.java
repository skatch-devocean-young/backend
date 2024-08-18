package devocean.tickit.global.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException { // exception custom을 위한 class

    private final ErrorCode errorCode;

    // 로그 필요 시 error message 참고하면 됨
    public String getMessage() {
        return errorCode.getMessage();
    }
}
