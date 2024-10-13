package devocean.tickit.dto.auth;

import jakarta.validation.constraints.NotNull;

public record AuthenticationRequestDto(
        @NotNull String idToken,
        @NotNull String provider
) {
}
