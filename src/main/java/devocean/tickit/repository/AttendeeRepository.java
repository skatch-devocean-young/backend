package devocean.tickit.repository;

import devocean.tickit.domain.Attendee;
import devocean.tickit.domain.Event;
import devocean.tickit.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
    @Query("SELECT a FROM Attendee a WHERE a.event = ?1 AND a.user = ?2")
    Optional<Attendee> findByEventAndUser(Event event, User user);

    @Query("SELECT a FROM Attendee a WHERE a.user.id = :uid AND a.registerStatus = 'ACCEPTED'")
    List<Attendee> findAcceptedByUid(Long uid);

    @Query("SELECT a FROM Attendee a WHERE a.user.id = :uid AND a.event.id = :eventId AND a.registerStatus = 'ACCEPTED'")
    Optional<Attendee> findByUser(Long uid, Long eventId);
}