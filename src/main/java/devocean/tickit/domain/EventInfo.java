package devocean.tickit.domain;

import devocean.tickit.global.BaseEntity;
import devocean.tickit.global.constant.ProgressStatus;
import devocean.tickit.global.constant.Registration;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "event_infos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_infos_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "events_id", foreignKey = @ForeignKey(name = "event_infos_fk_events_id"))
    private Event event;

    @Column(name = "content", nullable = false, length = 2000)
    private String content;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration", nullable = false)
    private Registration registration;

    @Builder
    public EventInfo(Event event, String content, LocalDateTime startDate, LocalDateTime endDate){
        this.event = event;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.registration = Registration.MANUAL;
    }
}