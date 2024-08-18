package devocean.tickit.global.api;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice // 전체 controller에 대해 적용하기 위한 어노테이션
public class ResponseStatusCodeSetterAdvice implements ResponseBodyAdvice<ApiResponse<?>> {

    // filter: supports == true일 때만 beforeBodyWrite 작동
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 응답값이 ApiResponse일 때만 custom을 실행하도록 check
        return returnType.getParameterType() == ApiResponse.class;
    }

    // controller return 전 response filtering
    @Override
    public ApiResponse<?> beforeBodyWrite(ApiResponse body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (returnType.getParameterType() == ApiResponse.class) {
            // set header status code
            HttpStatus status = body.httpStatus();
            response.setStatusCode(status);
        }

        return body;
    }
}
