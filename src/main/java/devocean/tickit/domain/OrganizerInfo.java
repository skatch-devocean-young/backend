package devocean.tickit.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "organizer_infos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrganizerInfo {

    @Id
    @Column(name = "organizer_info_id")
    private Long id; // PK이자 FK

    @OneToOne
    @MapsId
    @JoinColumn(name = "organizer_info_id")
    private User user;

    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "genre")
    private String genre;

    @Column(name = "title_song")
    private String titleSong;

    @Column(name = "member")
    private String member;

    @Column(name = "title_ment")
    private String titleMent;

    @Column(name = "profile_img")
    private String profileImg;

    @Builder
    public OrganizerInfo(User user, String artistName, String genre, String titleSong, String member, String titleMent, String profileImg) {
        this.user = user;
        this.artistName = artistName;
        this.genre = genre;
        this.titleSong = titleSong;
        this.member = member;
        this.titleMent = titleMent;
        this.profileImg = profileImg;
    }
}
