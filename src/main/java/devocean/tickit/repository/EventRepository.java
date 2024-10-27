package devocean.tickit.repository;

import devocean.tickit.domain.Event;
import devocean.tickit.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByUser(User user);
}