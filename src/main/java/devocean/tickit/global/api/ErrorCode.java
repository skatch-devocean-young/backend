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
    // 06
    INVALID_TEST_USER(40106, HttpStatus.UNAUTHORIZED, "존재하지 않는 테스트 사용자"),

    // 404 Not Found
    // 00
    NOT_FOUND(40400, HttpStatus.NOT_FOUND, "존재하지 않음"),
    // 01
    NOT_FOUND_END_POINT(40401, HttpStatus.NOT_FOUND, "존재하지 않는 API"),

    // 409
    // 00
    // 01
    DUPLICATED_ID(40901, HttpStatus.CONFLICT, "ID 중복"),

    // 500 Internal Server Error
    // 00
    INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류"),

    // 600 User Error
    // 01
    _NOT_FOUND_USER(60001, HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),

    // 700 Event Error
    // 01
    _ONLY_HOST_CAN_REGISTER_EVENT(70001, HttpStatus.BAD_REQUEST, "주최자만 행사를 등록할 수 있습니다."),
    ;

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}
