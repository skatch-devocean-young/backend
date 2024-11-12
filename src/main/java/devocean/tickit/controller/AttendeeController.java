package devocean.tickit.controller;

import devocean.tickit.dto.attendee.request.ApplyEventRequest;
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
    public ApiResponse<?> apply(@PathVariable("events_id") Long eventId, @RequestBody ApplyEventRequest request) {
        return attendeeService.applyEvent(eventId, request);
    }

    @PatchMapping("/{events_id}")
    public ApiResponse<?> admit(@PathVariable("events_id") Long eventId, @RequestBody ApplyEventRequest request) {
        return attendeeService.acceptAttendee(eventId, request);
    }
}
