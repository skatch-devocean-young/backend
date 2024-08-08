package devocean.tickit.repository;

import devocean.tickit.domain.Hash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashRepository extends JpaRepository<Hash, Long> {
}