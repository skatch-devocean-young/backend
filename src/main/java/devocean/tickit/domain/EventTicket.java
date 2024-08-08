package devocean.tickit.domain;

import devocean.tickit.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "event_tickets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventTicket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_tickets_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "events_id", foreignKey = @ForeignKey(name = "event_tickets_fk_events_id"))
    private Event event;

    @Column(name = "source_url", nullable = false, length = 2050)
    private String sourceUrl;

    @Column(name = "background_img_url", length = 2050)
    private String backgroundImgUrl;

    @Builder
    public EventTicket(Event event, String sourceUrl, String backgroundImgUrl){
        this.event = event;
        this.sourceUrl = sourceUrl;
        this.backgroundImgUrl = backgroundImgUrl;
    }
}