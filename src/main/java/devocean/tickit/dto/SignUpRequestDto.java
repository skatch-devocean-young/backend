package devocean.tickit.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record SignUpRequestDto(
        String name,
        String phone,
        String artistName,
        String genre,
        String member,
        String title_song,
        String title_ment,
        MultipartFile profile_img
) {
}
