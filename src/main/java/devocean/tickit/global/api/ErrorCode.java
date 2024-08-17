package devocean.tickit.global.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 000 분류가 필요한 에러
    // 00
    UNCUSTOMED_ERROR(00000, HttpStatus.OK, "서버 에러 커스텀이 필요"),

    // 404 Not Found
    // 00
    NOT_FOUND(40400, HttpStatus.NOT_FOUND, "존재하지 않음"),
    NOT_FOUND_END_POINT(40401, HttpStatus.NOT_FOUND, "존재하지 않는 API"),

    // 500 Internal Server Error
    // 00
    INTERBAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류"),


    ;

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
