package devocean.tickit.domain;

import devocean.tickit.global.BaseEntity;
import devocean.tickit.global.constant.ProgressStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "events")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "events_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", foreignKey = @ForeignKey(name = "events_fk_users_id"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hosts_id", foreignKey = @ForeignKey(name = "events_fk_hosts_id"))
    private Host host;

    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @Column(name = "description", nullable = false, length = 300)
    private String description;

    @Column(name = "entry_fee", nullable = false)
    private Long entryFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "progress_status", nullable = false)
    private ProgressStatus progressStatus;

    @Column(name = "update_reason", length = 2000)
    private String updateReason;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendee> attendees;

    @OneToMany(mappedBy = "event")
    private List<EventHash> eventHashes;

    @OneToMany(mappedBy = "event")
    private List<Star> stars;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private EventInfo eventInfo;

    @OneToMany(mappedBy = "event")
    private List<EventImg> eventImgs;

    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private EventPlace eventPlace;

    @OneToOne(mappedBy = "event")
    private EventTicket eventTicket;

    @Builder
    public Event(User user, Host host, String title, String description){
        this.user = user;
        this.host = host;
        this.title = title;
        this.description = description;
        this.entryFee = 0L;
        this.progressStatus = ProgressStatus.WANTED;
    }
}