package devocean.tickit.global.exception;

import devocean.tickit.global.api.ErrorCode;

public class ObjectNotFoundException extends CustomException {
    public ObjectNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
