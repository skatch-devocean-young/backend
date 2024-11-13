package devocean.tickit.repository;

import devocean.tickit.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE t.attendee.id = :attendeeId")
    Optional<Ticket> findByAttendeeId(Long attendeeId);

    @Query("SELECT t FROM Ticket t WHERE t.attendee.user.id = :uid")
    List<Ticket> findAllByUid(Long uid);
}