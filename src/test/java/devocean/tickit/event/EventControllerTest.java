package devocean.tickit.event;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import devocean.tickit.configuration.ControllerTestConfig;
import devocean.tickit.controller.EventController;
import devocean.tickit.dto.event.request.AddUserEventRequest;
import devocean.tickit.dto.event.request.ModifyUserEventRequest;
import devocean.tickit.dto.event.response.GetAllUserEventsResponse;
import devocean.tickit.dto.event.response.GetUserEventDetailResponse;
import devocean.tickit.global.constant.ProgressStatus;
import devocean.tickit.global.jwt.JwtUtils;
import devocean.tickit.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventControllerTest extends ControllerTestConfig {

    @MockBean
    private EventService eventService;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("주최자가 새로운 행사를 등록한다")
    public void addUserEvent() throws Exception {
        AddUserEventRequest request = new AddUserEventRequest(
                "Winter Folk Music Gathering 2024",
                "2024-12-30T17:00:00",
                "2024-12-30T21:00:00",
                "2024-11-20T00:00:00",
                "2024-12-25T23:59:59",
                "2024-11-20T00:00:00",
                "2024-12-20T23:59:59",
                25000,
                "Namsan Outdoor Stage",
                150,
                "추운 겨울, 따뜻한 포크 음악과 함께하는 감동의 무대!",
                "겨울의 낭만을 담은 포크 음악 공연"
        );

        Mockito.doNothing().when(eventService).addUserEvent(Mockito.anyLong(), Mockito.any(AddUserEventRequest.class));

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/v1/events/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        result
                .andExpect(status().isCreated())
                .andDo(MockMvcRestDocumentationWrapper.document("events/add",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Event")
                                .description("주최자가 새로운 행사를 등록한다.")
                                .pathParameters(parameterWithName("userId").description("사용자 ID [예시 : 1 (NUMBER Type)]"))
                                .requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("행사명"),
                                        fieldWithPath("eventStartDate").type(JsonFieldType.STRING).description("행사 시작 날짜"),
                                        fieldWithPath("eventEndDate").type(JsonFieldType.STRING).description("행사 종료 날짜"),
                                        fieldWithPath("bookingStartDate").type(JsonFieldType.STRING).description("예약 시작 날짜"),
                                        fieldWithPath("bookingEndDate").type(JsonFieldType.STRING).description("예약 종료 날짜"),
                                        fieldWithPath("paymentStartDate").type(JsonFieldType.STRING).description("결제 시작 날짜"),
                                        fieldWithPath("paymentEndDate").type(JsonFieldType.STRING).description("결제 종료 날짜"),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                        fieldWithPath("place").type(JsonFieldType.STRING).description("장소"),
                                        fieldWithPath("capacity").type(JsonFieldType.NUMBER).description("수용 인원"),
                                        fieldWithPath("comment").type(JsonFieldType.STRING).description("주최자 한마디"),
                                        fieldWithPath("description").type(JsonFieldType.STRING).description("상세 설명")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터").optional(),
                                        fieldWithPath("error").type(JsonFieldType.OBJECT).description("에러 정보").optional()
                                )
                                .requestSchema(Schema.schema("AddUserEventRequestDto"))
                                .responseSchema(Schema.schema("AddUserEventResponseDto"))
                                .build()
                        )));
    }

    @Test
    @DisplayName("주최자가 등록한 모든 행사 정보를 조회한다")
    public void getAllUserEvents() throws Exception {
        List<GetAllUserEventsResponse> response = List.of(new GetAllUserEventsResponse(
                1L,
                "Winter Folk Music Gathering 2024",
                LocalDateTime.parse("2024-12-30T17:00:00"),
                LocalDateTime.parse("2024-12-30T21:00:00"),
                "Namsan Outdoor Stage",
                150,
                LocalDateTime.parse("2024-11-20T00:00:00"),
                LocalDateTime.parse("2024-12-25T23:59:59"),
                "example.jpg"
        ));

        Mockito.when(eventService.getAllUserEvents(Mockito.anyLong())).thenReturn(response);

        ResultActions resultActions = this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/events/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].eventId").value(1))
                .andExpect(jsonPath("$.data[0].title").value("Winter Folk Music Gathering 2024"))
                .andExpect(jsonPath("$.data[0].place").value("Namsan Outdoor Stage"))

                .andDo(MockMvcRestDocumentationWrapper.document("events/get-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("Event")
                                        .description("주최자가 등록한 모든 행사 정보를 조회한다.")
                                        .pathParameters(
                                                parameterWithName("userId").description("사용자 ID [예시 : 1 (NUMBER Type)]")
                                        )
                                        .responseFields(
                                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                                fieldWithPath("data").type(JsonFieldType.ARRAY).description("행사 목록"),
                                                fieldWithPath("data[].eventId").type(JsonFieldType.NUMBER).description("행사 ID"),
                                                fieldWithPath("data[].title").type(JsonFieldType.STRING).description("행사 제목"),
                                                fieldWithPath("data[].eventStartDate").type(JsonFieldType.STRING).description("행사 시작일"),
                                                fieldWithPath("data[].eventEndDate").type(JsonFieldType.STRING).description("행사 종료일"),
                                                fieldWithPath("data[].place").type(JsonFieldType.STRING).description("행사 장소"),
                                                fieldWithPath("data[].capacity").type(JsonFieldType.NUMBER).description("수용 인원"),
                                                fieldWithPath("data[].bookingStartDate").type(JsonFieldType.STRING).description("예약 시작일"),
                                                fieldWithPath("data[].bookingEndDate").type(JsonFieldType.STRING).description("예약 종료일"),
                                                fieldWithPath("data[].postImgUrl").type(JsonFieldType.STRING).description("행사 포스터 이미지 URL"),
                                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보")
                                        )
                                        .responseSchema(Schema.schema("GetAllUserEventsResponseDto"))
                                        .build()
                        )
                ));
    }

    @Test
    @DisplayName("특정 행사에 대한 상세 정보를 조회한다")
    public void getUserEventDetail() throws Exception {
        GetUserEventDetailResponse response = new GetUserEventDetailResponse(
                "Winter Folk Music Gathering 2024",
                LocalDateTime.parse("2024-12-30T17:00:00"),
                LocalDateTime.parse("2024-12-30T21:00:00"),
                LocalDateTime.parse("2024-11-20T00:00:00"),
                LocalDateTime.parse("2024-12-25T23:59:59"),
                LocalDateTime.parse("2024-11-20T00:00:00"),
                LocalDateTime.parse("2024-12-20T23:59:59"),
                25000,
                "Namsan Outdoor Stage",
                150,
                "추운 겨울, 따뜻한 포크 음악과 함께하는 감동의 무대!",
                "겨울의 낭만을 담은 포크 음악 공연",
                ProgressStatus.WANTED,
                "example.jpg"
        );

        Mockito.when(eventService.getUserEventDetail(Mockito.anyLong(), Mockito.anyLong())).thenReturn(response);

        ResultActions resultActions = this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/v1/events/{eventId}/{userId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Winter Folk Music Gathering 2024"))
                .andExpect(jsonPath("$.data.place").value("Namsan Outdoor Stage"))

                .andDo(MockMvcRestDocumentationWrapper.document("events/get-detail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("Event")
                                        .description("특정 행사에 대한 상세 정보를 조회한다.")
                                        .pathParameters(
                                                parameterWithName("eventId").description("행사 ID [예시 : 1 (NUMBER Type)]"),
                                                parameterWithName("userId").description("사용자 ID [예시 : 1 (NUMBER Type)]")
                                        )
                                        .responseFields(
                                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("행사 상세 정보"),
                                                fieldWithPath("data.title").type(JsonFieldType.STRING).description("행사 제목"),
                                                fieldWithPath("data.eventStartDate").type(JsonFieldType.STRING).description("행사 시작일"),
                                                fieldWithPath("data.eventEndDate").type(JsonFieldType.STRING).description("행사 종료일"),
                                                fieldWithPath("data.bookingStartDate").type(JsonFieldType.STRING).description("예약 시작일"),
                                                fieldWithPath("data.bookingEndDate").type(JsonFieldType.STRING).description("예약 종료일"),
                                                fieldWithPath("data.paymentStartDate").type(JsonFieldType.STRING).description("결제 시작일"),
                                                fieldWithPath("data.paymentEndDate").type(JsonFieldType.STRING).description("결제 종료일"),
                                                fieldWithPath("data.price").type(JsonFieldType.NUMBER).description("가격"),
                                                fieldWithPath("data.place").type(JsonFieldType.STRING).description("장소"),
                                                fieldWithPath("data.capacity").type(JsonFieldType.NUMBER).description("수용 인원"),
                                                fieldWithPath("data.comment").type(JsonFieldType.STRING).description("주최자 한마디"),
                                                fieldWithPath("data.description").type(JsonFieldType.STRING).description("상세 설명"),
                                                fieldWithPath("data.progressStatus").type(JsonFieldType.STRING).description("진행 상태"),
                                                fieldWithPath("data.postImgUrl").type(JsonFieldType.STRING).description("행사 포스터 이미지 URL"),
                                                fieldWithPath("error").type(JsonFieldType.NULL).description("오류 정보")
                                        )
                                        .responseSchema(Schema.schema("GetUserEventDetailResponseDto"))
                                        .build()
                        )
                ));
    }

    @Test
    @DisplayName("주최자가 행사를 수정한다")
    public void updateUserEvent() throws Exception {
        ModifyUserEventRequest request = new ModifyUserEventRequest(
                "Winter Folk Music Gathering 2024 - Updated",
                LocalDateTime.parse("2024-12-31T17:00:00"),
                LocalDateTime.parse("2024-12-31T21:00:00"),
                LocalDateTime.parse("2024-11-21T00:00:00"),
                LocalDateTime.parse("2024-12-26T23:59:59"),
                LocalDateTime.parse("2024-11-21T00:00:00"),
                LocalDateTime.parse("2024-12-21T23:59:59"),
                30000,
                "Updated Namsan Outdoor Stage",
                200,
                "새로운 업데이트 메시지",
                "업데이트된 상세 설명"
        );

        Mockito.doNothing().when(eventService).modifyUserEvent(Mockito.anyLong(), Mockito.any(ModifyUserEventRequest.class), Mockito.anyLong());

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.patch("/api/v1/events/{eventId}/{userId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        result
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("events/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Event")
                                .description("주최자가 행사를 수정한다.")
                                .pathParameters(
                                        parameterWithName("userId").description("사용자 ID [예시 : 1 (NUMBER Type)]"),
                                        parameterWithName("eventId").description("행사 ID [예시 : 1 (NUMBER Type)]")
                                )
                                .requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("행사명"),
                                        fieldWithPath("eventStartDate").type(JsonFieldType.STRING).description("행사 시작 날짜"),
                                        fieldWithPath("eventEndDate").type(JsonFieldType.STRING).description("행사 종료 날짜"),
                                        fieldWithPath("bookingStartDate").type(JsonFieldType.STRING).description("예약 시작 날짜"),
                                        fieldWithPath("bookingEndDate").type(JsonFieldType.STRING).description("예약 종료 날짜"),
                                        fieldWithPath("paymentStartDate").type(JsonFieldType.STRING).description("결제 시작 날짜"),
                                        fieldWithPath("paymentEndDate").type(JsonFieldType.STRING).description("결제 종료 날짜"),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                                        fieldWithPath("place").type(JsonFieldType.STRING).description("장소"),
                                        fieldWithPath("capacity").type(JsonFieldType.NUMBER).description("수용 인원"),
                                        fieldWithPath("comment").type(JsonFieldType.STRING).description("주최자 한마디"),
                                        fieldWithPath("description").type(JsonFieldType.STRING).description("상세 설명")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터").optional(),
                                        fieldWithPath("error").type(JsonFieldType.OBJECT).description("에러 정보").optional()
                                )
                                .requestSchema(Schema.schema("ModifyUserEventRequestDto"))
                                .responseSchema(Schema.schema("ModifyUserEventResponseDto"))
                                .build()
                        )));
    }

    @Test
    @DisplayName("주최자가 행사를 삭제한다")
    public void deleteUserEvent() throws Exception {
        Mockito.doNothing().when(eventService).removeUserEvent(Mockito.anyLong(), Mockito.anyLong());

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/v1/events/{eventId}/{userId}", 1L, 1L)
        );

        result
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("events/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Event")
                                .description("주최자가 행사를 삭제한다.")
                                .pathParameters(
                                        parameterWithName("userId").description("사용자 ID [예시 : 1 (NUMBER Type)]"),
                                        parameterWithName("eventId").description("행사 ID [예시 : 1 (NUMBER Type)]")
                                )
                                .responseFields(
                                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("성공 여부"),
                                        fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터 없음"),
                                        fieldWithPath("error").type(JsonFieldType.OBJECT).description("에러 정보").optional()
                                )
                                .responseSchema(Schema.schema("DeleteUserEventResponseDto"))
                                .build()
                        )));
    }
}