package devocean.tickit.domain;

import devocean.tickit.global.BaseEntity;
import devocean.tickit.global.constant.RegisterStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "attendees")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendees_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "events_id", foreignKey = @ForeignKey(name = "attendees_fk_events_id"))
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", foreignKey = @ForeignKey(name = "attendees_fk_users_id"))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "register_status", nullable = false)
    private RegisterStatus registerStatus;

    @Column(name = "is_attended", nullable = false)
    private Boolean isAttended;

    @OneToOne(mappedBy = "attendee")
    private Ticket ticket;

    @Builder
    public Attendee(Event event, User user){
        this.event = event;
        this.user = user;
        this.registerStatus = RegisterStatus.ONHOLD;
        this.isAttended = false;
    }
}