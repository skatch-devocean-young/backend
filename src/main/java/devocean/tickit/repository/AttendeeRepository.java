package devocean.tickit.repository;

import devocean.tickit.domain.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
}