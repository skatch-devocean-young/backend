package devocean.tickit.dto.event.response;

import devocean.tickit.domain.Event;
import devocean.tickit.global.constant.ProgressStatus;

import java.time.LocalDateTime;

public record GetUserEventDetailResponse(
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
        String description,
        ProgressStatus progressStatus,
        String postImgUrl
) {
    public static GetUserEventDetailResponse from(Event event) {
        return new GetUserEventDetailResponse(
                event.getTitle(),
                event.getEventStartDate(),
                event.getEventEndDate(),
                event.getBookingStartDate(),
                event.getBookingEndDate(),
                event.getPaymentStartDate(),
                event.getPaymentEndDate(),
                event.getPrice(),
                event.getPlace(),
                event.getCapacity(),
                event.getComment(),
                event.getDescription(),
                event.getProgressStatus(),
                event.getPosterImgUrl()
        );
    }
}