package devocean.tickit.controller;

import devocean.tickit.dto.attendee.request.ApplyEventRequest;
import devocean.tickit.dto.attendee.request.AttendEventRequest;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.service.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/events/action-apply")
@RequiredArgsConstructor
@RestController
public class AttendeeController {

    private final AttendeeService attendeeService;

    @PostMapping("/{events_id}")
    public ApiResponse<Object> apply(@PathVariable("events_id") Long eventId, @RequestBody ApplyEventRequest request) {
        return attendeeService.applyEvent(eventId, request);
    }

    @PatchMapping("/{events_id}")
    public ApiResponse<Object> admit(@PathVariable("events_id") Long eventId, @RequestBody ApplyEventRequest request) {
        return attendeeService.acceptAttendee(eventId, request);
    }

    // qr 출석 체크
    @PostMapping("/attend")
    public ApiResponse<Object> attend(@RequestBody AttendEventRequest request) {
        return attendeeService.attendEvent(request);
    }
}
