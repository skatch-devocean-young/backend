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
@Table(name = "hashs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hash extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashs_id")
    private Long id;

    @Column(name = "keyword", nullable = false, length = 30)
    private String keyword;

    @OneToMany(mappedBy = "hash")
    private List<EventHash> eventHashes;

    @Builder
    public Hash(String keyword){
        this.keyword = keyword;
    }
}