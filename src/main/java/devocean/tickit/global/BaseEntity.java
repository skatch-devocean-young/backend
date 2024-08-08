package devocean.tickit.global;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(name = "created_at", length = 20)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", length = 20)
    private LocalDateTime updatedAt;
}