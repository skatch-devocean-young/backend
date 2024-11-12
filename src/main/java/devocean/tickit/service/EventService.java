package devocean.tickit.service;

import devocean.tickit.domain.Event;
import devocean.tickit.domain.User;
import devocean.tickit.dto.event.request.AddUserEventRequest;
import devocean.tickit.dto.event.request.ModifyUserEventRequest;
import devocean.tickit.dto.event.response.GetAllUserEventsResponse;
import devocean.tickit.dto.event.response.GetUserEventDetailResponse;
import devocean.tickit.global.api.ErrorCode;
import devocean.tickit.global.constant.Role;
import devocean.tickit.global.exception.CustomException;
import devocean.tickit.repository.EventRepository;
import devocean.tickit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final StorageService storageService;

    // 주최자가 행사를 등록하는 메서드
    @Transactional
    public void addUserEvent(Long userId, AddUserEventRequest addUserEventRequest, MultipartFile multipartFile) throws IOException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode._NOT_FOUND_USER));
        if (!Role.ORGANIZER.equals(user.getRole())) {
            throw new CustomException(ErrorCode._ONLY_HOST_CAN_REGISTER_EVENT);
        }
        String posterImgUrl = storageService.uploadFile(multipartFile);
        Event event = addUserEventRequest.toEntity(user, posterImgUrl);

        eventRepository.save(event);
    }

    // 주최자가 본인의 모든 이벤트를 조회하는 메서드
    @Transactional(readOnly = true)
    public List<GetAllUserEventsResponse> getAllUserEvents(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode._NOT_FOUND_USER));
        if (!Role.ORGANIZER.equals(user.getRole())) {
            throw new CustomException(ErrorCode._ONLY_HOST_CAN_VIEW_MY_EVENT);
        }

        return eventRepository.findAllByUser(user).stream()
                .map(GetAllUserEventsResponse::from)
                .toList();
    }

    // 주최자가 본인의 특정 행사를 상세 조회하는 메서드
    @Transactional(readOnly = true)
    public GetUserEventDetailResponse getUserEventDetail(Long userId, Long eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode._NOT_FOUND_USER));
        if (!Role.ORGANIZER.equals(user.getRole())) {
            throw new CustomException(ErrorCode._ONLY_HOST_CAN_VIEW_MY_EVENT);
        }

        return GetUserEventDetailResponse.from(eventRepository.findByUserAndId(user, eventId)
                .orElseThrow(() -> new CustomException(ErrorCode._NOT_FOUND_EVENT)));
    }

    // 주최자가 특정 행사를 수정하는 메서드
    @Transactional
    public void modifyUserEvent(Long userId, ModifyUserEventRequest request, Long eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode._NOT_FOUND_USER));
        if (!Role.ORGANIZER.equals(user.getRole())) {
            throw new CustomException(ErrorCode._ONLY_HOST_CAN_REGISTER_EVENT);
        }
        Event event = eventRepository.findByUserAndId(user, eventId)
                .orElseThrow(() -> new CustomException(ErrorCode._NOT_FOUND_EVENT));

        if (request.title() != null) {
            event.updateTitle(request.title());
        }
        if (request.eventStartDate() != null) {
            event.updateEventStartDate(request.eventStartDate());
        }
        if (request.eventEndDate() != null) {
            event.updateEventEndDate(request.eventEndDate());
        }
        if (request.bookingStartDate() != null) {
            event.updateBookingStartDate(request.bookingStartDate());
        }
        if (request.bookingEndDate() != null) {
            event.updateBookingEndDate(request.bookingEndDate());
        }
        if (request.paymentStartDate() != null) {
            event.updatePaymentStartDate(request.paymentStartDate());
        }
        if (request.paymentEndDate() != null) {
            event.updatePaymentEndDate(request.paymentEndDate());
        }
        if (request.price() != 0) {
            event.updatePrice(request.price());
        }
        if (request.place() != null) {
            event.updatePlace(request.place());
        }
        if (request.capacity() != 0) {
            event.updateCapacity(request.capacity());
        }
        if (request.comment() != null) {
            event.updateComment(request.comment());
        }
        if (request.description() != null) {
            event.updateDescription(request.description());
        }

        eventRepository.save(event);
    }

    // 주최자가 특정 행사를 삭제하는 메서드
    @Transactional
    public void removeUserEvent(Long userId, Long eventId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode._NOT_FOUND_USER));
        if (!Role.ORGANIZER.equals(user.getRole())) {
            throw new CustomException(ErrorCode._ONLY_HOST_CAN_REGISTER_EVENT);
        }
        Event event = eventRepository.findByUserAndId(user, eventId)
                .orElseThrow(() -> new CustomException(ErrorCode._NOT_FOUND_EVENT));
        eventRepository.delete(event);
    }
}