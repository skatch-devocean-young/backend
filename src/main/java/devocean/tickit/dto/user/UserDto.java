package devocean.tickit.dto.user;

import devocean.tickit.global.constant.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserDto(
        @NotNull Long id,
        @NotNull Role role
        ) {
}
