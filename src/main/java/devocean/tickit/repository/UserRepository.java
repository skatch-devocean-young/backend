package devocean.tickit.repository;

import devocean.tickit.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProviderId(String providerId);

    @Query("SELECT u FROM User u WHERE u.provider = ?1 AND u.providerId = ?2")
    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    Optional<User> findById(Long id);
}