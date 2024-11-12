package devocean.tickit.dto.attendee.request;

import jakarta.validation.constraints.NotNull;

public record ApplyEventRequest(
        @NotNull Long uid
) {
}
