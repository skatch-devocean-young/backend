package devocean.tickit.global.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

public record ApiResponse<T>(
        @JsonIgnore HttpStatus httpStatus,
        boolean success,
        @Nullable T data,
        @Nullable ExceptionDto error
        ) {

        public static <T> ApiResponse<T> ok(@Nullable final T data) {
                return new ApiResponse<>(HttpStatus.OK, true, data, null);
        }

        public static <T> ApiResponse<T> created(@Nullable final T data) {
                return new ApiResponse<>(HttpStatus.CREATED, true, data, null);
        }

        public static <T> ApiResponse<T> failed(final ErrorCode errorCode) {
                return new ApiResponse<>(errorCode.getHttpStatus(), false, null, ExceptionDto.of(errorCode));
        }

        public static ResponseEntity<Object> onFailure(final ErrorCode errorCode, String message) {
                ExceptionDto response = ExceptionDto.containMessageOf(errorCode, message);
                return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
        }
}
