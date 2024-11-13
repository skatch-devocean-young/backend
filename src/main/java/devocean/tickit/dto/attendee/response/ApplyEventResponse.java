package devocean.tickit.dto.attendee.response;

import devocean.tickit.domain.Attendee;

public record ApplyEventResponse(
        Long attendeeId
) {
    public static ApplyEventResponse from(Attendee attendee) {
        return new ApplyEventResponse(attendee.getId());
    }
}
