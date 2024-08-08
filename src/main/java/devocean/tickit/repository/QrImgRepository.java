package devocean.tickit.repository;

import devocean.tickit.domain.QrImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QrImgRepository extends JpaRepository<QrImg, Long> {
}