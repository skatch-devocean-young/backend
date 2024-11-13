package devocean.tickit.service;

import devocean.tickit.domain.Attendee;
import devocean.tickit.domain.Ticket;
import devocean.tickit.dto.ticket.request.TicketRequest;
import devocean.tickit.dto.ticket.response.TicketResponse;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.global.api.ErrorCode;
import devocean.tickit.global.constant.RegisterStatus;
import devocean.tickit.repository.AttendeeRepository;
import devocean.tickit.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final AttendeeRepository attendeeRepository;
    private final QrCodeService qrCodeService;

    // 티켓 앨범 조회
    @Transactional
    public ApiResponse<?> getList(TicketRequest request) throws Exception {
        try {
            // 신청 행사 중 accept 된 행사만 조회
            Long uid = request.uid();
            List<Attendee> attendeeList = attendeeRepository.findAcceptedByUid(uid);

            // 신청 행사 중 티켓 없는 행사의 티켓 발급
            for(Attendee attendee : attendeeList) {
                // 티켓 레포 조회
                Optional<Ticket> optionalTicket = ticketRepository.findByAttendeeId(attendee.getId());
                Ticket ticket = optionalTicket.orElse(null);


                // 티켓 없으면 새로 생성
                if(ticket == null) {
                    ticket = createTicket(attendee.getId());
                }
            }

            // 티켓 레포 조회
            List<Ticket> ticketList = ticketRepository.findAllByUid(request.uid());
            List<TicketResponse> ticketResponseList = new ArrayList<>();
            for(Ticket ticket : ticketList) {
                TicketResponse tmpTicket = new TicketResponse(ticket.getId(), ticket.getImg_url(), ticket.getTicketStatus().toString(), ticket.getQrImg().getImg_url());
                ticketResponseList.add(tmpTicket);
            }

            return ApiResponse.ok(ticketResponseList);
        } catch (Exception e) {
            return ApiResponse.failed(ErrorCode.OBJECT_NOT_FOUND);
        }
    }

    // 개별 티켓 조회
    @Transactional
    public ApiResponse<?> getTicket(Long attendeeId) throws Exception {
        try {
            // 티켓 레포 조회
            Optional<Ticket> optionalTicket = ticketRepository.findByAttendeeId(attendeeId);
            Ticket ticket = optionalTicket.orElse(null);


            // 티켓 없으면 새로 생성
            if(ticket == null) {
                ticket = createTicket(attendeeId);
            }

            // responseDto 생성
            TicketResponse response = new TicketResponse(ticket.getId(), ticket.getImg_url(), ticket.getTicketStatus().toString(), ticket.getQrImg().getImg_url());
            return ApiResponse.created(response);

        } catch (Exception e) {
            return ApiResponse.failed(ErrorCode.OBJECT_NOT_FOUND);
        }
    }

    // 새로운 티켓 생성
    @Transactional
    public Ticket createTicket(Long attendeeId) throws Exception {
        // 행사 신청 상태 확인(accept인지)
        Optional<Attendee> attendee = attendeeRepository.findById(attendeeId);
        if(!attendee.get().getRegisterStatus().equals(RegisterStatus.ACCEPTED)) {
            throw new Exception(ErrorCode.OBJECT_NOT_FOUND.getMessage());
        }

        // 티켓 qr 생성
        String qrImg = qrCodeService.generateAndUploadQRCode(attendee.get().getEvent().getId(), attendee.get().getUser().getId());
        Ticket ticket = new Ticket(attendee.get(), attendee.get().getEvent().getPosterImgUrl());

        // 새로운 티켓 저장
        ticketRepository.save(ticket);
        attendee.get().setTicket(ticket);
        attendeeRepository.save(attendee.get());

        // 새로운 티켓 반환
        return ticket;
    }
}
