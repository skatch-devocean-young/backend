package devocean.tickit.dto.ticket.request;

import jakarta.validation.constraints.NotNull;

public record TicketRequest(
        @NotNull Long uid
) {
}
