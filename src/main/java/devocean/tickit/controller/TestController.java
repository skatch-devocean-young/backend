package devocean.tickit.controller;

import devocean.tickit.dto.TestLogInRequestDto;
import devocean.tickit.dto.TestSignInRequestDto;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@RestController
public class TestController {

    private final TestService testService;

    // 구글 인증 없이 회원가입 for DEV
    @PostMapping("/signup")
    public ApiResponse<?> signUp(@RequestBody TestSignInRequestDto requestDto) {
        return testService.signInforDev(requestDto);
    }

    // 구글 인증 없이 로그인 for DEV
    @PostMapping("/login")
    public ApiResponse<?> logIn(@RequestBody TestLogInRequestDto requestDto) {
        return testService.logInfotDev(requestDto);
    }

    // 헬스 체크용
    @GetMapping("/health")
    public String healthCheck() {
        return "Hello World!";
    }
}
