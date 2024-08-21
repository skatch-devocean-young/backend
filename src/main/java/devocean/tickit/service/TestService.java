package devocean.tickit.service;

import devocean.tickit.domain.User;
import devocean.tickit.domain.UserInfo;
import devocean.tickit.dto.AuthenticationResponseDto;
import devocean.tickit.dto.TestLogInRequestDto;
import devocean.tickit.dto.TestSignInRequestDto;
import devocean.tickit.dto.UserDto;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.global.api.ErrorCode;
import devocean.tickit.global.constant.Role;
import devocean.tickit.global.jwt.JwtUtils;
import devocean.tickit.repository.UserInfoRepository;
import devocean.tickit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestService {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public ApiResponse<?> signInforDev(TestSignInRequestDto requestDto) {

        // 가입되어 있는 값이면 error
        if (userRepository.findByProviderAndProviderId(requestDto.provider(), requestDto.providerId()).isPresent()) {
            return ApiResponse.failed(ErrorCode.DUPLICATED_ID);
        }
        signUp(requestDto.name(), requestDto.provider(), requestDto.providerId());

        // jwt 생성을 위한 dto 생성
        Optional<User> user = userRepository.findByProviderAndProviderId(requestDto.provider(), requestDto.providerId());
        UserDto userDto = UserDto.builder()
                .id(user.get().getId())
                .role(Role.ATTENDEE)
                .build();

        // user uuid & role로 jwt 생성
        AuthenticationResponseDto responseDto = AuthenticationResponseDto.builder()
                .accessToken(jwtUtils.createAccessToken(userDto))
                .refreshToken(jwtUtils.createRefreshToken(userDto))
                .name(user.get().getName())
                .build();

        return ApiResponse.created(responseDto);
    }

    private void signUp(String name, String provider, String providerId) {
        User user = User.builder()
                .name(name)
                .provider(provider)
                .providerId(providerId)
                .imgUrl("/////default img url value로 수정 필요/////")
                .role(Role.ATTENDEE)
                .build();
        userRepository.save(user);

        // 최초 가입 시 함께 생성되어야 하는 tables : UserInfo
        UserInfo userInfo = UserInfo.builder().build().setInitialUserInfo(user);
        userInfoRepository.save(userInfo);

        log.info("사용자 회원가입 완료");
    }

    public ApiResponse<?> logInfotDev(TestLogInRequestDto requestDto) {
        Optional<User> optionalUser = userRepository.findByProviderAndProviderId(requestDto.provider(), requestDto.providerId());

        if (optionalUser.isEmpty()) {
            return ApiResponse.failed(ErrorCode.INVALID_TEST_USER);
        }

        UserDto userDto = UserDto.builder()
                .id(optionalUser.get().getId())
                .role(optionalUser.get().getRole())
                .build();

        // 기존에 발급했던 refreshToken block하기
        jwtUtils.deleteRefreshTokenById(requestDto.providerId());

        // 새로운 토큰 생성
        AuthenticationResponseDto responseDto = AuthenticationResponseDto.builder()
                .accessToken(jwtUtils.createAccessToken(userDto))
                .refreshToken(jwtUtils.createRefreshToken(userDto))
                .name(optionalUser.get().getName())
                .build();

        return ApiResponse.created(responseDto);
    }
}
