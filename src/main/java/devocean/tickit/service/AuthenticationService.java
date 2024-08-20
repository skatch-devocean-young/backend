package devocean.tickit.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import devocean.tickit.domain.User;
import devocean.tickit.domain.UserInfo;
import devocean.tickit.dto.AuthenticationResponseDto;
import devocean.tickit.dto.AuthenticationRequestDto;
import devocean.tickit.dto.UserDto;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.global.api.ErrorCode;
import devocean.tickit.global.api.UnauthorizedException;
import devocean.tickit.global.constant.Role;
import devocean.tickit.global.jwt.JwtUtils;
import devocean.tickit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    @Value("${google.clientid}")
    private String CLIENT_ID;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public ApiResponse<?> authenticateUsers(AuthenticationRequestDto requestDto) {

        // 토큰 유효성 검사
        GoogleIdToken tokenDto = verifyUser(requestDto.idToken());
        if(tokenDto == null) {
            return ApiResponse.failed(ErrorCode.INVALID_GOOGLE_TOKEN);
        }

        // ID 토큰으로 프로필 정보 열람
        GoogleIdToken.Payload payload = tokenDto.getPayload();
        String name = payload.get("given_name").toString();
        String providerId = payload.getSubject(); // a key to identify a user

        // providerId로 user 탐색
//        Optional<User> user = userRepository.findByProviderId(providerId);
        // providerId와 provider로 user 탐색
        Optional<User> user = userRepository.findByProviderAndProviderId(requestDto.provider(), providerId);

        // 신규 user면 회원가입 (name, provider, provider_id, role, img_url)
        user.ifPresentOrElse(
                // Optional 객체 존재하는 경우
                existingUser -> {
                    log.info("기가입유저 -> {}", existingUser);
                },
                // Optional 객체 비어있는 경우
                () -> {
                    log.info("신규가입유저");
                    signUp(name, requestDto.provider(), providerId);
                }
        );

        // jwt 생성을 위한 dto 생성
        user = userRepository.findByProviderAndProviderId("google", providerId);
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

        log.info("사용자 회원가입 완료");
    }

    private GoogleIdToken verifyUser(String googleToken) {

        log.info("googleToken 검증 함수 진입");
        GoogleIdToken idToken = null;

        try {
            idToken = GoogleIdToken.parse(new JacksonFactory(), googleToken);
            log.info("token 해체 -> {}", idToken);
        } catch (IOException e) {
            log.info("token 해체 실패");
            return null;
        }

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                // Specify the CLIENT_ID of the app that access the backend
                .setAudience(Collections.singletonList(CLIENT_ID))
                // or, if multiple client access the backend
//                .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        log.info("verifier 생성 -> {}", String.valueOf(verifier));

        try {
            // google token 검증 (SDK)
            idToken = verifier.verify(googleToken);
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        if (idToken != null) {
            log.info("idToken is not null");
            GoogleIdToken.Payload payload = idToken.getPayload();

            // a key to identify a user
            String googleId = payload.getSubject();
            log.info("Google unique user ID : " + googleId);
        } else  {
            log.info("Token is NULL");
            // exception
            throw new UnauthorizedException(ErrorCode.INVALID_GOOGLE_TOKEN);
        }

        return idToken;
    }
}
