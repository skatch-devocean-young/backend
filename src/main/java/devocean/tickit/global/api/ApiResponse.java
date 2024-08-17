package devocean.tickit.global.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;
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

        public static <T> ApiResponse<T> failed(final CustomException e) {
                return new ApiResponse<>(e.getErrorCode().getHttpStatus(), false, null, ExceptionDto.of(e.getErrorCode()));
        }
}
