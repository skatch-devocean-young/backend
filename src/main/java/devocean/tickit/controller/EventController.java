package devocean.tickit.controller;

import devocean.tickit.dto.event.request.AddUserEventRequest;
import devocean.tickit.dto.event.request.ModifyUserEventRequest;
import devocean.tickit.dto.event.response.GetAllUserEventsResponse;
import devocean.tickit.dto.event.response.GetUserEventDetailResponse;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@RestController
public class EventController {

    private final EventService eventService;

    // 주최자 행사 등록 API
    @PostMapping
    public ApiResponse<Object> addUserEvent(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestPart("addEventRequestDto") AddUserEventRequest addEventRequestDto,
            @RequestPart("file") MultipartFile multipartFile) throws IOException {

        eventService.addUserEvent(authorizationHeader, addEventRequestDto, multipartFile);
        return ApiResponse.created(null);
    }

    // 주최자 행사 전체 조회 API
    @GetMapping
    public ApiResponse<List<GetAllUserEventsResponse>> getAllUserEvents(
            @RequestHeader("Authorization") String authorizationHeader){

        List<GetAllUserEventsResponse> response = eventService.getAllUserEvents(authorizationHeader);
        return ApiResponse.ok(response);
    }

    // 주최자 특정 행사 상세 조회 API
    @GetMapping("/{eventId}")
    public ApiResponse<GetUserEventDetailResponse> getUserEventDetail(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("eventId") Long eventId){

        GetUserEventDetailResponse response = eventService.getUserEventDetail(authorizationHeader, eventId);
        return ApiResponse.ok(response);
    }

    // 주최자 특정 행사 수정 API
    @PatchMapping("/{eventId}")
    public ApiResponse<Object> modifyUserEvent(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody ModifyUserEventRequest request,
            @PathVariable("eventId") Long eventId){

        eventService.modifyUserEvent(authorizationHeader, request, eventId);
        return ApiResponse.ok(null);
    }

    // 주최자 특정 행사 삭제 API
    @DeleteMapping("/{eventId}")
    public ApiResponse<Object> removeUserEvent(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("eventId") Long eventId){

        eventService.removeUserEvent(authorizationHeader, eventId);
        return ApiResponse.ok(null);
    }
}