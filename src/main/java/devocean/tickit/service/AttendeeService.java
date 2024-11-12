package devocean.tickit.service;

import devocean.tickit.domain.Attendee;
import devocean.tickit.domain.Event;
import devocean.tickit.domain.User;
import devocean.tickit.dto.attendee.request.ApplyEventRequest;
import devocean.tickit.dto.attendee.response.ApplyEventResponse;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.repository.AttendeeRepository;
import devocean.tickit.repository.EventRepository;
import devocean.tickit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public ApiResponse<?> applyEvent(Long eventsId, ApplyEventRequest request) {
        // event id로 event 조회
        Optional<Event> event = eventRepository.findById(eventsId);

        // user id로 user 조회
        Optional<User> user = userRepository.findById(request.uid());

        // attendee 객체 생성(RegisterStatus == ONHOLD)
        Attendee attendee = new Attendee(event.get(), user.get());

        // response 객체 생성
        ApplyEventResponse response = ApplyEventResponse.from(attendee);

        return ApiResponse.created(response);
    }
}
