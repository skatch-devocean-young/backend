package devocean.tickit.dto.test;

import jakarta.validation.constraints.NotNull;

public record TestSignInRequestDto(
        @NotNull String name,
        @NotNull String provider,
        @NotNull String providerId
) {
}
