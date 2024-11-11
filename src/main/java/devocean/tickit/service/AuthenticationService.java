package devocean.tickit.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import devocean.tickit.domain.User;
import devocean.tickit.dto.auth.AuthenticationRequestDto;
import devocean.tickit.dto.auth.AuthenticationResponseDto;
import devocean.tickit.dto.user.UserDto;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.global.api.ErrorCode;
import devocean.tickit.global.exception.UnauthorizedException;
import devocean.tickit.global.constant.Role;
import devocean.tickit.global.jwt.JwtUtils;
import devocean.tickit.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    @Value("${google.clientid}")
    private String CLIENT_ID;
    @Value("${kakao.issuer}")
    private String KAKAO_ISSUER; // iss url
    @Value("${kakao.publickey}")
    private String KAKAO_PUBLIC_KEY; // 공개키 url
    @Value("${kakao.appkey}")
    private String KAKAO_APP_KEY; // aud (서비스 앱 키)
    @Value("${apple.issuer}")
    private String APPLE_ISSUER; // iss url
    @Value("${apple.publickey}")
    private String APPLE_PUBLIC_KEY; // 공개키 url
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    public ApiResponse<?> authenticateUsers(AuthenticationRequestDto requestDto) {

        try {
            String providerId = "";

            // provider 종류에 따라 토큰 유효성 검사
            // get providerId
            switch (requestDto.provider().toLowerCase()) {
                case "google":
                    GoogleIdToken tokenDto = verifyGoogleUser(requestDto.idToken());
                    if (tokenDto == null) {
                        throw new UnauthorizedException(ErrorCode.INVALID_SOCIAL_TOKEN);
                    }
                    providerId = tokenDto.getPayload().getSubject();
                    break;

                case "kakao":
                    providerId = verifyKakaoUser(requestDto.idToken());
                    if (providerId == null) {
                        throw new UnauthorizedException(ErrorCode.INVALID_SOCIAL_TOKEN);
                    }
                    break;

                case "apple":
                    providerId = verifyAppleUser(requestDto.idToken());
                    if (providerId == null) {
                        throw new UnauthorizedException(ErrorCode.INVALID_SOCIAL_TOKEN);
                    }
                    break;
            }

            // providerId와 provider로 user 탐색
            Optional<User> user = userRepository.findByProviderAndProviderId(requestDto.provider(), providerId);

            // response
            String registerToken = "";
            String accessToken = "";
            String refreshToken = "";
            String name = "";
            Boolean isRegistered = FALSE;

            // 신규 user면 회원가입 (name, provider, provider_id, role, img_url)
            if (user.isEmpty()) {
                log.info("신규가입유저");
                // registerToken 값 생성
                registerToken = jwtUtils.createRegisterToken(requestDto.provider(), providerId);
            }
            else {
                log.info("기가입유저");
                 AuthenticationResponseDto responseDto = getTokens(user.get());
                 accessToken = responseDto.accessToken();
                 refreshToken = responseDto.refreshToken();
                 name = responseDto.name();
                 isRegistered = TRUE;
            }

            return ApiResponse.created(AuthenticationResponseDto.builder()
                    .registerToken(registerToken)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .name(name)
                    .isRegistered(isRegistered)
            );

        } catch (UnauthorizedException e) {
            return ApiResponse.failed(e.getErrorCode());
        } catch (Exception e) {
            return ApiResponse.failed(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

//    private void signUp(String name, String provider, String providerId) {
//        User user = User.builder()
//                .name(name)
//                .provider(provider)
//                .providerId(providerId)
//                .imgUrl("/////default img url value로 수정 필요/////")
//                .role(Role.ATTENDEE)
//                .build();
//
//        userRepository.save(user);
//
//        log.info("사용자 회원가입 완료");
//    }

    private AuthenticationResponseDto getTokens(User user) {
        // jwt 생성을 위한 dto 생성
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .role(Role.ATTENDEE)
                .name(user.getName())
                .build();

        // user id & role로 jwt 생성
        AuthenticationResponseDto responseDto = AuthenticationResponseDto.builder()
                .refreshToken("")
                .accessToken(jwtUtils.createAccessToken(userDto))
                .refreshToken(jwtUtils.createRefreshToken(userDto))
                .isRegistered(TRUE)
                .build();

        return responseDto;
    }

    private GoogleIdToken verifyGoogleUser(String googleToken) {

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
            throw new UnauthorizedException(ErrorCode.INVALID_SOCIAL_TOKEN);
        }

        return idToken;
    }

    private String verifyKakaoUser(String kakaoToken) {
        log.info("kakaoToken 검증 함수 진입");

        String idToken = null;

        try {
            // 페이로드 검증
            // JWT 페이로드 parsing
            String[] tokenParts = kakaoToken.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]));

            log.info("payload : " + payload);

            // ObjectMapper
            Map<String, Object> payloadMap = new ObjectMapper().readValue(payload, Map.class);

            // iss, aud, exp 값 검증
            // nonce는 원래 검증해야 하지만,, pass,,
            if (!KAKAO_ISSUER.equals(payloadMap.get("iss")) || !KAKAO_APP_KEY.equals(payloadMap.get("aud")) || System.currentTimeMillis() / 1000 > (Integer) payloadMap.get("exp")) {
                throw new UnauthorizedException(ErrorCode.INVALID_SOCIAL_TOKEN);
            }

            // 서명 검증
            // 카카오 공개키 목록 조회
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> jwks = restTemplate.getForObject(KAKAO_PUBLIC_KEY, Map.class);

            // JWT 검증용 키 ID와 매칭되는 공개키 추출
            String kid = Jwts.parserBuilder().build().parseClaimsJws(idToken).getHeader().getKeyId();
            PublicKey publicKey = getAppleOrKakaoPublicKey(KAKAO_PUBLIC_KEY, kid);

            // 공개키로 JWT 서명 검증
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(idToken);
            if (claims == null) {
                throw new UnauthorizedException(ErrorCode.INVALID_SOCIAL_TOKEN);
            }
            return idToken;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String verifyAppleUser(String appleToken) throws JsonProcessingException {
        // ID 토큰의 헤더에서 alg와 kid를 추출
        Map<String, String> algAndKid = getAlgAndKidFromIdToken(appleToken);
        String alg = algAndKid.get("alg");
        String kid = algAndKid.get("kid");

        try {
            // 애플 공개키 목록에서 일치하는 공개키 검색
            PublicKey publicKey = getAppleOrKakaoPublicKey(APPLE_PUBLIC_KEY, kid);

            // 공개키를 사용해 토큰 서명 검증
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(appleToken);

            Claims body = claims.getBody();

            // 추가 검증: issuer, audience, expiration
            if (!APPLE_ISSUER.equals(body.getIssuer())) {
                throw new UnauthorizedException(ErrorCode.INVALID_SOCIAL_TOKEN);
            }

            if (!CLIENT_ID.equals(body.getAudience())) {
                throw new UnauthorizedException(ErrorCode.INVALID_SOCIAL_TOKEN);
            }

            if (body.getExpiration().before(new Date())) {
                throw new UnauthorizedException(ErrorCode.INVALID_SOCIAL_TOKEN);
            }

            // providerId 반환
            return body.getSubject();

        } catch (Exception e) {
            log.error("Apple token validation failed", e);
            throw new UnauthorizedException(ErrorCode.INVALID_SOCIAL_TOKEN);
        }
    }


    public PublicKey getAppleOrKakaoPublicKey(String jwksUrl, String kid) throws Exception {
        List<Map<String, Object>> keys = fetchJwksFromEndpoint(jwksUrl);
        return extractPublicKey(keys, kid);
    }

    private List<Map<String, Object>> fetchJwksFromEndpoint(String jwksUrl) throws Exception {
        HttpEntity<String> httpEntity = new HttpEntity<>(new HttpHeaders());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> res = restTemplate.exchange(jwksUrl, HttpMethod.GET, httpEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jwksContent = objectMapper.readTree(res.getBody());

        // "keys" 필드를 List<Map<String, Object>> 타입으로 변환
        return objectMapper.convertValue(jwksContent.get("keys"), List.class);
    }

    public PublicKey extractPublicKey(List<Map<String, Object>> keys, String kid) throws Exception {
        for (Object keyObj : keys) {
            JSONObject key = (JSONObject) keyObj;

            if (kid.equals(key.get("kid"))) {
                String n = (String) key.get("n");
                String e = (String) key.get("e");

                BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
                BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));
                RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, exponent);

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return keyFactory.generatePublic(spec);
            }
        }
        throw new IllegalArgumentException("kid에 해당하는 공개키를 찾을 수 없습니다.");
    }

    private Map<String, String> getAlgAndKidFromIdToken(String appleToken) throws JsonProcessingException {
        Map<String, String> algAndKid = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();

        String header = appleToken.split("\\.")[0];
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String decodedHeader = new String(decoder.decode(header));

        // Jackson을 사용해 JSON 파싱
        JsonNode headerContent = objectMapper.readTree(decodedHeader);

        algAndKid.put("alg", headerContent.get("alg").asText());
        algAndKid.put("kid", headerContent.get("kid").asText());

        return algAndKid;
    }
}
