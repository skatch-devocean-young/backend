package devocean.tickit.domain;

import devocean.tickit.global.BaseEntity;
import devocean.tickit.global.constant.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "provider", nullable = false, length = 20)
    private String provider;

    @Column(name = "provider_id", nullable = false, length = 50)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "img_url", nullable = false, length = 2050)
    private String imgUrl;

    @Column(name = "birthday")
    private LocalDateTime birthday;

    @Column(name = "phone", length = 11)
    private String phone;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendee> attendees;

    @OneToMany(mappedBy = "user")
    private List<Event> events;

    @Builder
    public User(String name, String provider, String providerId, Role role, String imgUrl, LocalDateTime birthday, String phone){
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
        this.birthday = birthday;
        this.phone = phone;
        this.role = Role.ATTENDEE;
        this.imgUrl = imgUrl;
    }
}