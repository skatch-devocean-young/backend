package devocean.tickit.attendee;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import devocean.tickit.configuration.ControllerTestConfig;
import devocean.tickit.controller.AttendeeController;
import devocean.tickit.dto.attendee.request.ApplyEventRequest;
import devocean.tickit.dto.attendee.request.AttendEventRequest;
import devocean.tickit.global.api.ApiResponse;
import devocean.tickit.global.constant.RegisterStatus;
import devocean.tickit.global.jwt.JwtUtils;
import devocean.tickit.service.AttendeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AttendeeController.class)
public class AttendeeControllerTest extends ControllerTestConfig {

    @MockBean
    private AttendeeService attendeeService;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("행사를 신청한다.")
    public void applyEvent() throws Exception {
        Long eventId = 1L;
        ApplyEventRequest request = new ApplyEventRequest(1L);

        when(attendeeService.applyEvent(any(Long.class), any(ApplyEventRequest.class)))
                .thenReturn(ApiResponse.created(null));

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/v1/events/action-apply/{events_id}", eventId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(MockMvcRestDocumentationWrapper.document("attendee/apply",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Attendee")
                                .description("행사를 신청한다.")
                                .pathParameters(parameterWithName("events_id").description("행사 ID [예시 : 1 (NUMBER Type)]"))
                                .requestFields(fieldWithPath("uid").type(JsonFieldType.NUMBER).description("사용자 ID"))
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터").optional(),
                                        fieldWithPath("error").type(JsonFieldType.OBJECT).description("에러 정보").optional()
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("행사 참석자를 승인한다.")
    public void admitAttendee() throws Exception {
        Long eventId = 1L;
        ApplyEventRequest request = new ApplyEventRequest(1L);

        when(attendeeService.acceptAttendee(eventId, request))
                .thenReturn(ApiResponse.ok(RegisterStatus.ACCEPTED));

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/api/v1/events/action-apply/{events_id}", eventId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(RegisterStatus.ACCEPTED.toString()))
                .andDo(MockMvcRestDocumentationWrapper.document("attendee/admit",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Attendee")
                                .description("행사 참석자를 승인한다.")
                                .pathParameters(parameterWithName("events_id").description("행사 ID [예시 : 1 (NUMBER Type)]"))
                                .requestFields(fieldWithPath("uid").type(JsonFieldType.NUMBER).description("사용자 ID"))
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data").type(JsonFieldType.STRING).description("승인 결과"),
                                        fieldWithPath("error").type(JsonFieldType.OBJECT).description("에러 정보").optional()
                                )
                                .build()
                        )));
    }

    @Test
    @DisplayName("QR 출석 체크를 한다.")
    public void attendEvent() throws Exception {
        AttendEventRequest request = new AttendEventRequest(1L, 1L);

        when(attendeeService.attendEvent(request))
                .thenReturn(ApiResponse.ok("출석 처리 되었습니다."));

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/v1/events/action-apply/attend")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("출석 처리 되었습니다."))
                .andDo(MockMvcRestDocumentationWrapper.document("attendee/attend",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Attendee")
                                .description("QR 출석 체크를 한다.")
                                .requestFields(
                                        fieldWithPath("uid").type(JsonFieldType.NUMBER).description("사용자 ID"),
                                        fieldWithPath("event_id").type(JsonFieldType.NUMBER).description("행사 ID")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data").type(JsonFieldType.STRING).description("출석 결과 메시지"),
                                        fieldWithPath("error").type(JsonFieldType.OBJECT).description("에러 정보").optional()
                                )
                                .build()
                        )));
    }
}