package devocean.tickit.repository;

import devocean.tickit.domain.EventImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventImgRepository extends JpaRepository<EventImg, Long> {
}