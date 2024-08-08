package devocean.tickit.repository;

import devocean.tickit.domain.Star;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRepository extends JpaRepository<Star, Long> {
}