package devocean.tickit.domain;

import devocean.tickit.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "hosts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Host extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hosts_id")
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "img_url", length = 2050)
    private String imgUrl;

    @Column(name = "link", length = 2050)
    private String link;

    @OneToMany(mappedBy = "host")
    private List<Event> events;

    @Builder
    public Host(String name, String email, String phone, String imgUrl, String link){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.imgUrl = imgUrl;
        this.link = link;
    }
}