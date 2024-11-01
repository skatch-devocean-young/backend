package devocean.tickit.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class StorageService {
    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    public String uploadFile(MultipartFile file) throws IOException {
        // 이미지명 uuid 변환
        String uuid = UUID.randomUUID().toString();

        // 이미지 파일 이름에서 확장자 추출
        String originalFileName = file.getOriginalFilename();
        String ext = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            ext = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        // Google Cloud Storage 이미지 업로드
        BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, uuid + ext)
                        .setContentType("image/jpeg")
                        .build(),
                file.getInputStream()
        );
        log.info("Google Cloud Storage에 이미지 업로드 성공");

        // 업로드된 이미지의 URL 생성
        return "https://storage.googleapis.com/" + bucketName + "/" + uuid + ext;
    }
}