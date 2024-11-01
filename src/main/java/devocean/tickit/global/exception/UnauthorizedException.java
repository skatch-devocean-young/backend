package devocean.tickit.global.exception;

import devocean.tickit.global.api.ErrorCode;
import devocean.tickit.global.exception.CustomException;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
