package devocean.tickit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AuthenticationResponseDto(
        @NotNull String accessToken,
        @NotNull String refreshToken,
        @NotNull String name
) {
}
