package devocean.tickit.service;

import devocean.tickit.domain.Attendee;
import devocean.tickit.domain.Event;
import devocean.tickit.domain.User;
import devocean.tickit.dto.attendee.request.ApplyEventRequest;
import devocean.tickit.dto.attendee.request.AttendEventRequest;
import devocean.tickit.dto.attendee.response.ApplyEventResponse;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.global.api.ErrorCode;
import devocean.tickit.repository.AttendeeRepository;
import devocean.tickit.repository.EventRepository;
import devocean.tickit.repository.TicketRepository;
import devocean.tickit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public ApiResponse<Object> applyEvent(Long eventsId, ApplyEventRequest request) {
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

    @Transactional
    public ApiResponse<Object> acceptAttendee(Long eventId, ApplyEventRequest request) {
        Optional<Event> event = eventRepository.findById(eventId);
        Optional<User> user = userRepository.findById(request.uid());
        Optional<Attendee> attendee = attendeeRepository.findByEventAndUser(event.get(), user.get());
        attendee.get().accept(attendee.get());
        attendeeRepository.save(attendee.get());

        return ApiResponse.ok(attendee.get().getRegisterStatus());
    }

    @Transactional
    public ApiResponse<Object> attendEvent(AttendEventRequest request) {
        try {
            // attendee 객체 조회
            Optional<Attendee> attendee = attendeeRepository.findByUser(request.uid(), request.event_id());
            if (attendee.isEmpty()) {
                return ApiResponse.failed(ErrorCode.OBJECT_NOT_FOUND);
            }

            // change status of attendee.isAttended from false to true
            attendeeRepository.save(attendee.get().attend());

            // change status of ticket.ticketStatus from UNUSED to USED
            ticketRepository.save(attendee.get().getTicket().attend());

            // return response
            return ApiResponse.ok("출석 처리 되었습니다.");

        } catch (Exception e) {
            return ApiResponse.failed(ErrorCode.OBJECT_NOT_FOUND);
        }
    }
}
