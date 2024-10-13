package devocean.tickit.controller;

import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.dto.auth.AuthenticationRequestDto;
import devocean.tickit.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    // 인증
    @PostMapping("/login")
    public ApiResponse<?> authenticateUsers(@RequestBody AuthenticationRequestDto requestDto) {
        return authenticationService.authenticateUsers(requestDto);
    }
}
