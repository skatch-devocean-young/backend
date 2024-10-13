package devocean.tickit.service;

import devocean.tickit.domain.Event;
import devocean.tickit.domain.User;
import devocean.tickit.dto.event.AddEventRequestDto;
import devocean.tickit.global.api.CustomException;
import devocean.tickit.global.api.ErrorCode;
import devocean.tickit.global.constant.Role;
import devocean.tickit.global.jwt.JwtUtils;
import devocean.tickit.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;
    private final JwtUtils jwtUtils;

    // 이벤트를 등록하는 메서드
    public void addEvent(String authorizationHeader, AddEventRequestDto addEventRequestDto) {

        User user = jwtUtils.getUserFromHeader(authorizationHeader);
        if (!Role.ORGANIZER.equals(user.getRole())) {
            throw new CustomException(ErrorCode._ONLY_HOST_CAN_REGISTER_EVENT);
        }
        Event event = addEventRequestDto.toEntity(user);

        eventRepository.save(event);
    }
}