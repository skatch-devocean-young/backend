package devocean.tickit.controller;

import devocean.tickit.dto.event.AddEventRequestDto;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@RestController
public class EventController {

    private final EventService eventService;

    // 행사 등록 API
    @PostMapping("/action-open")
    public ApiResponse<?> addEvent(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody AddEventRequestDto addEventRequestDto) {

        eventService.addEvent(authorizationHeader, addEventRequestDto);

        return ApiResponse.created(null);
    }
}