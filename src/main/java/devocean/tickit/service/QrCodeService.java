package devocean.tickit.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class QrCodeService {

    private final StorageService storageService;

    public String generateAndUploadQRCode(Long eventId, Long uid) throws Exception {
        // QR 코드 생성
        byte[] qrCodeBytes = generateQRCode(eventId, uid);

        // 파일 이름 설정
        String fileName = String.format("qr-%s-%s.png", eventId, uid);

        // ByteArray를 MultipartFile로 변환
        MultipartFile qrFile = FileService.convertToMultipartFile(qrCodeBytes, fileName);

        // StorageService를 이용해 GCS에 업로드
        return storageService.uploadFile(qrFile);
    }

    public byte[] generateQRCode(Long evnetId, Long uid) throws Exception {

        // JSON 형식의 데이터 생성
        String qrContent = String.format("{\"evnetId\":\"%s\", \"uid\":\"%s\"}", evnetId, uid);

        // QR 코드 생성 객체
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // 인코딩 힌트 설정
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // QR 코드 생성
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 100, 100, hints);

        // ByteArrayOutputStream을 사용해 이미지를 바이트 배열로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream.toByteArray();
    }
}
