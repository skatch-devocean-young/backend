package devocean.tickit.dto;

import jakarta.validation.constraints.NotNull;

public record AuthenticationRequestDto(
        @NotNull String idToken,
        @NotNull String provider
) {
}
