package devocean.tickit.dto.attendee.request;

import jakarta.validation.constraints.NotNull;

public record AttendEventRequest(
        @NotNull Long uid,
        @NotNull Long event_id
) {
}
