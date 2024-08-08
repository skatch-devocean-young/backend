package devocean.tickit.domain;

import devocean.tickit.global.BaseEntity;
import devocean.tickit.global.constant.TicketStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tickets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tickets_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendees_id", foreignKey = @ForeignKey(name = "tickets_fk_attendees_id"))
    private Attendee attendee;

    @Column(name = "img_url", length = 2050)
    private String img_url;

    @Enumerated(EnumType.STRING)
    @Column(name = "ticket_status", nullable = false)
    private TicketStatus ticketStatus;

    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private QrImg qrImg;

    @Builder
    public Ticket(Attendee attendee, String img_url){
        this.attendee = attendee;
        this.img_url = img_url;
        this.ticketStatus = TicketStatus.UNUSED;
    }
}