package devocean.tickit.repository;

import devocean.tickit.domain.EventInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventInfoRepository extends JpaRepository<EventInfo, Long> {
}