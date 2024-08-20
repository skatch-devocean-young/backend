package devocean.tickit.global.jwt;

import devocean.tickit.global.api.ErrorCode;
import devocean.tickit.global.api.UnauthorizedException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import devocean.tickit.dto.UserDto;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtils {
    @Value("${jwt.time.access}")
    private long accessTokenTime; // 30일
    @Value("${jwt.time.refresh}")
    private long refreshTokenTime; // 30일
    @Value("${jwt.key}")
    private String jwtSecretKey;
    private final StringRedisTemplate stringRedisTemplate;

    public String createAccessToken(UserDto userDto) {
        Claims claims = Jwts.claims();
        claims.put("id", userDto.id());
        claims.put("role", userDto.role().name());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

    public String createRefreshToken(UserDto userDto) {
        Claims claims = Jwts.claims();
        claims.put("id", userDto.id());
        Date now = new Date();
        String refreshToken =  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenTime))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();

        updateUserRefreshToken(userDto, refreshToken);
        return refreshToken;
    }

    public void updateUserRefreshToken(UserDto userDto, String refreshToken) {
        stringRedisTemplate.opsForValue().set(String.valueOf(userDto.id()), refreshToken, refreshTokenTime, TimeUnit.MILLISECONDS);
    }

    public String getUserRefreshToken(String id) {
        return stringRedisTemplate.opsForValue().get(id);
    }

    public void deleteRefreshTokenByName(String id) {
        if (getUserRefreshToken(id) != null) {
            stringRedisTemplate.delete(id);
        }
    }

    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new UnauthorizedException(ErrorCode.JWT_TOKEN_NOT_EXISTS);
        }
        if(isLogout(token)){
            throw new UnauthorizedException(ErrorCode.LOG_OUT_JWT_TOKEN);
        }
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
            log.info("token \"role\" : " + claims.get("role"));
            log.info("token \"name\" : " + claims.get("name"));
            return true;
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException(ErrorCode.INVALID_JWT);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(ErrorCode.JWT_EXPIRED);
        } catch (UnauthorizedException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = getClaims(token);

        if (claims.get("role") == null) {
            throw new UnauthorizedException(ErrorCode.INVALID_JWT);
        }

        // 클레임에서 권한 정보 취득
        String role = getRoleValueFromToken(token);

        // UserDetails 객체를 생성하여 Authentication 반환
        UserDetails principal = new User(getNameFromToken(token), "", Collections.singleton(new SimpleGrantedAuthority(role)));
        return new UsernamePasswordAuthenticationToken(principal, "", Collections.singleton(new SimpleGrantedAuthority(role)));
    }

    public void setBlackList(String accessToken) {
        Long expiration = getExpiration(accessToken);
        stringRedisTemplate.opsForValue().set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isLogout(String accessToken) {
        return !ObjectUtils.isEmpty(stringRedisTemplate.opsForValue().get(accessToken));
    }

    public Long getExpiration(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.getTime() - new Date().getTime();
    }

    public String getNameFromToken(String token) {
        return getClaims(token).get("name").toString();
    }

    public String getRoleValueFromToken(String token) {
        return getClaims(token).get("role").toString();
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
    }

    public String resolveJWT(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }
}
