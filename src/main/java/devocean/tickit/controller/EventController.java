package devocean.tickit.controller;

import devocean.tickit.dto.event.request.AddUserEventRequest;
import devocean.tickit.dto.event.request.ModifyUserEventRequest;
import devocean.tickit.dto.event.response.GetAllUserEventsResponse;
import devocean.tickit.dto.event.response.GetUserEventDetailResponse;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.service.EventService;
import jakarta.validation.Valid;
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

    /**
     * 주최자가 새로운 행사를 등록한다
     *
     * @param addUserEventRequest 행사 등록 요청 정보 (행사명, 날짜, 장소 등)
     * @param multipartFile 행사 이미지
     * @param userId  사용자 ID
     * @return 생성된 행사에 대한 응답
     * @throws IOException 이미지 파일 처리 중 오류가 발생할 경우
     */
    @PostMapping("/{userId}")
    public ApiResponse<Object> addUserEvent(
            @Valid @RequestPart("addUserEventRequest") AddUserEventRequest addUserEventRequest,
            @RequestPart("imageFile") MultipartFile multipartFile,
            @PathVariable("userId") Long userId) throws IOException {

        eventService.addUserEvent(userId, addUserEventRequest, multipartFile);
        return ApiResponse.created(null);
    }

    /**
     * 주최자가 등록한 모든 행사 정보를 조회한다.
     *
     * @param userId 사용자 ID
     * @return 주최자가 등록한 행사 목록
     */
    @GetMapping("/{userId}")
    public ApiResponse<List<GetAllUserEventsResponse>> getAllUserEvents(
            @PathVariable("userId") Long userId){

        List<GetAllUserEventsResponse> response = eventService.getAllUserEvents(userId);
        return ApiResponse.ok(response);
    }

    /**
     * 특정 행사에 대한 상세 정보를 조회한다.
     *
     * @param eventId 조회할 행사 ID
     * @param userId  사용자 ID
     * @return 특정 행사의 상세 정보
     */
    @GetMapping("/{eventId}/{userId}")
    public ApiResponse<GetUserEventDetailResponse> getUserEventDetail(
            @PathVariable("eventId") Long eventId,
            @PathVariable("userId") Long userId){

        GetUserEventDetailResponse response = eventService.getUserEventDetail(userId, eventId);
        return ApiResponse.ok(response);
    }

    /**
     * 주최자가 특정 행사 정보를 수정한다.
     *
     * @param request 행사 수정 요청 정보 (변경할 필드들)
     * @param eventId 수정할 행사 ID
     * @param userId  사용자 ID
     * @return 수정 성공 여부 응답
     */
    @PatchMapping("/{eventId}/{userId}")
    public ApiResponse<Object> modifyUserEvent(
            @RequestBody ModifyUserEventRequest request,
            @PathVariable("eventId") Long eventId,
            @PathVariable("userId") Long userId){

        eventService.modifyUserEvent(userId, request, eventId);
        return ApiResponse.ok(null);
    }

    /**
     * 주최자가 특정 행사를 삭제한다.
     *
     * @param eventId 삭제할 행사 ID
     * @param userId  사용자 ID
     * @return 삭제 성공 여부 응답
     */
    @DeleteMapping("/{eventId}/{userId}")
    public ApiResponse<Object> removeUserEvent(
            @PathVariable("eventId") Long eventId,
            @PathVariable("userId") Long userId){

        eventService.removeUserEvent(userId, eventId);
        return ApiResponse.ok(null);
    }
}