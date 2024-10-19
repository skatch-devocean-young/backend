package devocean.tickit.service;

import devocean.tickit.domain.Event;
import devocean.tickit.domain.User;
import devocean.tickit.dto.event.AddEventRequestDto;
import devocean.tickit.global.exception.CustomException;
import devocean.tickit.global.api.ErrorCode;
import devocean.tickit.global.constant.Role;
import devocean.tickit.global.jwt.JwtUtils;
import devocean.tickit.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final JwtUtils jwtUtils;
    private final StorageService storageService;

    // 이벤트를 등록하는 메서드
    @Transactional
    public void addEvent(String authorizationHeader, AddEventRequestDto addEventRequestDto, MultipartFile multipartFile) throws IOException {

        User user = jwtUtils.getUserFromHeader(authorizationHeader);
        if (!Role.ORGANIZER.equals(user.getRole())) {
            throw new CustomException(ErrorCode._ONLY_HOST_CAN_REGISTER_EVENT);
        }
        String posterImg = storageService.uploadFile(multipartFile);
        Event event = addEventRequestDto.toEntity(user, posterImg);

        eventRepository.save(event);
    }
}