package devocean.tickit.domain;

import devocean.tickit.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "qr_imgs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QrImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qr_imgs_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tickets_id", foreignKey = @ForeignKey(name = "qr_imgs_fk_tickets_id"))
    private Ticket ticket;

    @Column(name = "img_url", nullable = false, length = 2050)
    private String img_url;

    @Builder
    public QrImg(Ticket ticket, String img_url){
        this.ticket = ticket;
        this.img_url = img_url;
    }
}