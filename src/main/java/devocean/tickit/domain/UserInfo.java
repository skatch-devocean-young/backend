package devocean.tickit.domain;

import devocean.tickit.global.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "user_infos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_infos_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", foreignKey = @ForeignKey(name = "user_infos_fk_users_id"))
    private User user;

    @Column(name = "nickname", length = 20)
    private String nickname;

    @Column(name = "birthday")
    private LocalDateTime birthday;

    @Column(name = "job", length = 20)
    private String job;

    @Column(name = "phone", length = 11)
    private String phone;

    @Builder
    public UserInfo(User user, String nickname, LocalDateTime birthday, String job, String phone){
        this.user = user;
        this.nickname = nickname;
        this.birthday = birthday;
        this.job = job;
        this.phone = phone;
    }
}