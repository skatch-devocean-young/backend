package devocean.tickit.ticket;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import devocean.tickit.configuration.ControllerTestConfig;
import devocean.tickit.controller.TicketController;
import devocean.tickit.dto.ticket.request.TicketRequest;
import devocean.tickit.dto.ticket.response.TicketResponse;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.global.jwt.JwtUtils;
import devocean.tickit.service.TicketService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
public class TicketControllerTest extends ControllerTestConfig {

    @MockBean
    private TicketService ticketService;

    @MockBean
    private JwtUtils jwtUtils;
    @Test
    @DisplayName("사용자의 티켓 리스트를 조회한다.")
    public void getTicketList() throws Exception {
        List<TicketResponse> ticketResponseList = List.of(
                new TicketResponse(1L, "imageUrl1", "UNUSED", "qrImageUrl1"),
                new TicketResponse(2L, "imageUrl2", "USED", "qrImageUrl2")
        );

        when(ticketService.getList(any(Long.class)))  // PathVariable에 맞게 변경
                .thenReturn(ApiResponse.ok(ticketResponseList));

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/ticket/list/{uid}", 2L)  // PathVariable 전달
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].img_url").value("imageUrl1"))
                .andExpect(jsonPath("$.data[0].status").value("UNUSED"))
                .andExpect(jsonPath("$.data[0].qr_img_url").value("qrImageUrl1"))
                .andDo(MockMvcRestDocumentationWrapper.document("ticket/get-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Ticket")
                                .description("사용자의 티켓 리스트를 조회한다.")
                                .pathParameters(  // PathVariable 문서화
                                        parameterWithName("uid").description("사용자 ID")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data[].ticket_id").type(JsonFieldType.NUMBER).description("티켓 ID"),
                                        fieldWithPath("data[].img_url").type(JsonFieldType.STRING).description("티켓 이미지 URL"),
                                        fieldWithPath("data[].status").type(JsonFieldType.STRING).description("티켓 상태"),
                                        fieldWithPath("data[].qr_img_url").type(JsonFieldType.STRING).description("QR 이미지 URL"),
                                        fieldWithPath("error").type(JsonFieldType.OBJECT).description("에러 정보").optional()
                                ).build()
                        )));
    }

    @Test
    @DisplayName("개별 티켓을 상세 조회한다.")
    public void getTicketInfo() throws Exception {
        Long attendeeId = 1L;
        TicketResponse ticketResponse = new TicketResponse(1L, "imageUrl", "UNUSED", "qrImageUrl");

        when(ticketService.getTicket(attendeeId))
                .thenReturn(ApiResponse.ok(ticketResponse));

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/ticket/{attendee_id}", attendeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.img_url").value("imageUrl"))
                .andExpect(jsonPath("$.data.status").value("UNUSED"))
                .andExpect(jsonPath("$.data.qr_img_url").value("qrImageUrl"))
                .andDo(MockMvcRestDocumentationWrapper.document("ticket/get-info",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Ticket")
                                .description("개별 티켓을 상세 조회한다.")
                                .pathParameters(parameterWithName("attendee_id").description("참가자 ID [예시 : 1 (NUMBER Type)]"))
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data.ticket_id").type(JsonFieldType.NUMBER).description("티켓 ID"),
                                        fieldWithPath("data.img_url").type(JsonFieldType.STRING).description("티켓 이미지 URL"),
                                        fieldWithPath("data.status").type(JsonFieldType.STRING).description("티켓 상태"),
                                        fieldWithPath("data.qr_img_url").type(JsonFieldType.STRING).description("QR 이미지 URL"),
                                        fieldWithPath("error").type(JsonFieldType.OBJECT).description("에러 정보").optional()
                                ).build()
                        )));
    }
}