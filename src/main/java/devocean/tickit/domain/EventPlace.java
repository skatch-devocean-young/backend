package devocean.tickit.domain;

import devocean.tickit.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "event_places")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventPlace extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_places_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "events_id", foreignKey = @ForeignKey(name = "event_places_fk_events_id"))
    private Event event;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "address", nullable = false, length = 1000)
    private String address;

    @Builder
    public EventPlace(Event event, String name, String address){
        this.event = event;
        this.name = name;
        this.address = address;
    }
}