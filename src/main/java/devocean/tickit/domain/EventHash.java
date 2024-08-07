package devocean.tickit.domain;

import devocean.tickit.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "event_hashs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventHash extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_hashs_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "events_id", foreignKey = @ForeignKey(name = "event_hashs_fk_events_id"))
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashs_id", foreignKey = @ForeignKey(name = "event_hashs_fk_hashs_id"))
    private Hash hash;

    @Builder
    public EventHash(Event event, Hash hash){
        this.event = event;
        this.hash = hash;
    }
}