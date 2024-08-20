package devocean.tickit.dto;

import jakarta.validation.constraints.NotNull;

public record TestLogInRequestDto(
        @NotNull String provider,
        @NotNull String providerId
) {
}
