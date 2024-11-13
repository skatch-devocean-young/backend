package devocean.tickit.dto.ticket.response;

import jakarta.validation.constraints.NotNull;

public record TicketResponse(
        @NotNull Long ticket_id,
        @NotNull String img_url,
        @NotNull String status,
        @NotNull String qr_img_url
) {
}
