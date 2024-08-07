package devocean.tickit.domain;

import devocean.tickit.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "event_imgs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_imgs_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "events_id", foreignKey = @ForeignKey(name = "event_imgs_fk_events_id"))
    private Event event;

    @Column(name = "img_order", nullable = false)
    private Long imgOrder;

    @Column(name = "img_url", nullable = false, length = 2050)
    private String imgUrl;

    @Builder
    public EventImg(Event event, Long imgOrder, String imgUrl){
        this.event = event;
        this.imgOrder = imgOrder;
        this.imgUrl = imgUrl;
    }
}