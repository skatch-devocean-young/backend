package devocean.tickit.dto.user;

import devocean.tickit.global.constant.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserDto(
        @NotNull UUID id,
        @NotNull Role role
        ) {
}
