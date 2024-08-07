package devocean.tickit.repository;

import devocean.tickit.domain.EventHash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventHashRepository extends JpaRepository<EventHash, Long> {
}