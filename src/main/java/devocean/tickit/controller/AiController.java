package devocean.tickit.controller;

import devocean.tickit.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@RestController
public class AiController {

    private final AiService aiService;

    /**
     * 배경 제거 API
     * 사용자가 업로드한 이미지에서 배경을 제거하고, 결과 이미지를 반환합니다.
     *
     * @param imageFile 배경 제거를 위한 원본 이미지 파일
     * @return 배경이 제거된 이미지 파일을 포함한 응답
     */
    @PostMapping(value = "/removeback", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> removeBackground(
            @RequestPart("imageFile") MultipartFile imageFile) {

        return aiService.removeBackground(imageFile);
    }

    /**
     * 이미지 생성 API
     * 사용자가 업로드한 포스터 이미지를 기반으로 생성된 이미지를 반환합니다.
     *
     * @param imageFile 이미지 생성을 위한 포스터 이미지 파일
     * @return 생성된 이미지 파일을 포함한 응답
     */
    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> generateImage(
            @RequestPart("imageFile") MultipartFile imageFile) {
        return aiService.generateImage(imageFile);
    }
}