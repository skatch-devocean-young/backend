package devocean.tickit.controller;

import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/ticket")
@RequiredArgsConstructor
@RestController
public class TicketController {

    private final TicketService ticketService;

    // 티켓 리스트 조회
    @GetMapping("/list/{uid}")
    public ApiResponse<Object> getTicketList(@PathVariable("uid") Long uid) throws Exception {
        return ticketService.getList(uid);
    }

    // 개별 티켓 조회
    @GetMapping("/{attendee_id}")
    public ApiResponse<Object> getTicketInfo(@PathVariable("attendee_id") Long attendeeId) throws Exception {
        return ticketService.getTicket(attendeeId);
    }
}
