package devocean.tickit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AiService {

    private final WebClient webClient;
    private final String FLASK_BASE_URL = "http://ai-server:5001"; // Flask 컨테이너의 주소 및 포트

    // 배경 제거 API 호출 메서드
    public ResponseEntity<Resource> removeBackground(MultipartFile contentImage) {
        String endpoint = "/api/v1/tickets/removeback";
        return sendFileToFlask(contentImage, endpoint, "content_image");
    }

    // 이미지 생성 API 호출 메서드
    public ResponseEntity<Resource> generateImage(MultipartFile poster) {
        String endpoint = "/generate";
        return sendFileToFlask(poster, endpoint, "poster");
    }

    // 공통 Flask API 호출 메서드
    private ResponseEntity<Resource> sendFileToFlask(MultipartFile file, String endpoint, String fileParamName) {
        try {
            // MultipartBodyBuilder를 사용하여 파일 데이터를 설정
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            bodyBuilder.part(fileParamName, file.getBytes()).filename(file.getOriginalFilename());

            // Flask API에 파일을 전송
            Mono<ResponseEntity<byte[]>> responseMono = webClient.post()
                    .uri(FLASK_BASE_URL + endpoint)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(bodyBuilder.build())
                    .retrieve()
                    .toEntity(byte[].class);

            // 비동기 요청을 블로킹하여 응답을 기다림
            ResponseEntity<byte[]> response = responseMono.block();

            if (response != null && response.getBody() != null) {
                // Flask로부터 받은 이미지 데이터를 클라이언트에게 반환
                ByteArrayResource resource = new ByteArrayResource(response.getBody());
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.setContentType(MediaType.IMAGE_PNG);
                return new ResponseEntity<>(resource, responseHeaders, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (WebClientResponseException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.valueOf(e.getRawStatusCode()));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}