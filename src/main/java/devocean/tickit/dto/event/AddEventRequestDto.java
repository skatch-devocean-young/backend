package devocean.tickit.dto.event;

import devocean.tickit.domain.Event;
import devocean.tickit.domain.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AddEventRequestDto(
        @NotNull(message = "행사명은 필수값입니다.") String title,
        @NotNull(message = "행사 시작 시각은 필수값입니다.") String eventStartDate,
        @NotNull(message = "행사 종료 시각은 필수값입니다.") String eventEndDate,
        @NotNull(message = "예매 시작 시각은 필수값입니다.") String bookingStartDate,
        @NotNull(message = "예매 종료 시각은 필수값입니다.") String bookingEndDate,
        @NotNull(message = "입금 시작 시각은 필수값입니다.") String paymentStartDate,
        @NotNull(message = "입금 종료 시각은 필수값입니다.") String paymentEndDate,
        @NotNull(message = "금액은 필수값입니다.") int price,
        @NotNull(message = "장소는 필수값입니다.") String place,
        @NotNull(message = "수용 인원은 필수값입니다.") int capacity,
        @NotNull(message = "주최자 한마디는 필수값입니다.") String comment,
        @NotNull(message = "상세 설명은 필수값입니다.") String description
) {
        public Event toEntity(User user) {
                return Event.builder()
                        .user(user)
                        .title(title)
                        .eventStartDate(LocalDateTime.parse(eventStartDate))
                        .eventEndDate(LocalDateTime.parse(eventEndDate))
                        .bookingStartDate(LocalDateTime.parse(bookingStartDate))
                        .bookingEndDate(LocalDateTime.parse(bookingEndDate))
                        .paymentStartDate(LocalDateTime.parse(paymentStartDate))
                        .paymentEndDate(LocalDateTime.parse(paymentEndDate))
                        .price(price)
                        .place(place)
                        .capacity(capacity)
                        .comment(comment)
                        .description(description)
                        .build();
        }
}
