package devocean.tickit.dto.event.response;

import devocean.tickit.domain.Event;

import java.time.LocalDateTime;


public record GetAllUserEventsResponse(
        Long eventId,
        String title,
        LocalDateTime eventStartDate,
        LocalDateTime eventEndDate,
        String place,
        int capacity,
        LocalDateTime bookingStartDate,
        LocalDateTime bookingEndDate,
        String postImgUrl
) {
    public static GetAllUserEventsResponse from(Event event) {
        return new GetAllUserEventsResponse(
                event.getId(),
                event.getTitle(),
                event.getEventStartDate(),
                event.getEventEndDate(),
                event.getPlace(),
                event.getCapacity(),
                event.getBookingStartDate(),
                event.getBookingEndDate(),
                event.getPosterImgUrl());
    }
}