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

    // 401 UNAUTHORIZED
    // 01
    INVALID_GOOGLE_TOKEN(40101, HttpStatus.UNAUTHORIZED, "유효하지 않은 Google ID Token"),
    // 02
    JWT_TOKEN_NOT_EXISTS(40102, HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않음"),
    // 03
    LOG_OUT_JWT_TOKEN(40103, HttpStatus.UNAUTHORIZED, "로그아웃된 사용자"),
    // 04
    INVALID_JWT(40104, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰"),
    // 05
    JWT_EXPIRED(40105, HttpStatus.UNAUTHORIZED, "만료된 토큰"),

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
