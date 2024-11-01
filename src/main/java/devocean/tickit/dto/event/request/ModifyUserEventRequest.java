package devocean.tickit.dto.event.request;

import java.time.LocalDateTime;

public record ModifyUserEventRequest(
        String title,
        LocalDateTime eventStartDate,
        LocalDateTime eventEndDate,
        LocalDateTime bookingStartDate,
        LocalDateTime bookingEndDate,
        LocalDateTime paymentStartDate,
        LocalDateTime paymentEndDate,
        int price,
        String place,
        int capacity,
        String comment,
        String description
) {
}