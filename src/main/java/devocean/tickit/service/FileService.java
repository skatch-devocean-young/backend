package devocean.tickit.service;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class FileService {

    public static MultipartFile convertToMultipartFile(byte[] fileContent, String fileName) throws IOException {
        return new MockMultipartFile(
                fileName,
                fileName,
                "image/png",  // QR 코드 이미지는 보통 PNG로 생성됩니다
                new ByteArrayInputStream(fileContent)
        );
    }
}

