package devocean.tickit.service;

import devocean.tickit.domain.Ticket;
import devocean.tickit.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TicketService {
    private final TicketRepository ticketRepository;

    // 티켓 발급
    //public Ticket createTicket() {

    //}
}
