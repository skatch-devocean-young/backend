package devocean.tickit.repository;

import devocean.tickit.domain.EventPlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventPlaceRepository extends JpaRepository<EventPlace, Long> {
}