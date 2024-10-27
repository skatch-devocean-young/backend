package devocean.tickit.controller;

import devocean.tickit.dto.event.AddEventRequestDto;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@RestController
public class EventController {

    private final EventService eventService;

    // 행사 등록 API
    @PostMapping
    public ApiResponse<?> addEvent(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestPart("addEventRequestDto") AddEventRequestDto addEventRequestDto,
            @RequestPart("file") MultipartFile multipartFile) throws IOException {

        eventService.addEvent(authorizationHeader, addEventRequestDto, multipartFile);

        return ApiResponse.created(null);
    }
}