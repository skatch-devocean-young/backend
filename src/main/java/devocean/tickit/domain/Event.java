package devocean.tickit.domain;

import devocean.tickit.global.BaseEntity;
import devocean.tickit.global.constant.ProgressStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "event_start_date", nullable = false)
    private LocalDateTime eventStartDate;

    @Column(name = "event_end_date", nullable = false)
    private LocalDateTime eventEndDate;

    @Column(name = "booking_start_date", nullable = false)
    private LocalDateTime bookingStartDate;

    @Column(name = "booking_end_date", nullable = false)
    private LocalDateTime bookingEndDate;

    @Column(name = "payment_start_date", nullable = false)
    private LocalDateTime paymentStartDate;

    @Column(name = "payment_end_date", nullable = false)
    private LocalDateTime paymentEndDate;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "place", nullable = false, length = 100)
    private String place;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "comment", length = 300)
    private String comment;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "progress_status", nullable = false)
    private ProgressStatus progressStatus;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendee> attendees;

    @OneToMany(mappedBy = "event")
    private List<EventImg> eventImgs;

    @Builder
    public Event(User user, String title, LocalDateTime eventStartDate, LocalDateTime eventEndDate, LocalDateTime bookingStartDate, LocalDateTime bookingEndDate, LocalDateTime paymentStartDate, LocalDateTime paymentEndDate, int price, String place, int capacity, String comment, String description){
        this.user = user;
        this.title = title;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.bookingStartDate = bookingStartDate;
        this.bookingEndDate = bookingEndDate;
        this.paymentStartDate = paymentStartDate;
        this.paymentEndDate = paymentEndDate;
        this.price = price;
        this.place = place;
        this.capacity = capacity;
        this.comment = comment;
        this.description = description;
        this.progressStatus = ProgressStatus.WANTED;
    }
}