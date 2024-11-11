package devocean.tickit.dto.auth;

import lombok.Builder;

@Builder
public record AuthenticationResponseDto(
        String registerToken,
        String accessToken,
        String refreshToken,
        String name,
        Boolean isRegistered
) {
}
