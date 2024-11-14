package devocean.tickit.ai;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import devocean.tickit.configuration.ControllerTestConfig;
import devocean.tickit.controller.AiController;
import devocean.tickit.global.jwt.JwtUtils;
import devocean.tickit.service.AiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AiController.class)
public class AiControllerTest extends ControllerTestConfig {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AiService aiService;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("배경 제거 API 테스트")
    @WithMockUser
    public void removeBackgroundTest() throws Exception {
        MockMultipartFile imageFile = createMockImageFile();
        Resource resource = new ByteArrayResource("image data".getBytes());
        Mockito.when(aiService.removeBackground(Mockito.any(MultipartFile.class)))
                .thenReturn(ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource));

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.multipart("/api/v1/ai/removeback")
                        .file(imageFile)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        result
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document(
                        "{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("AI")
                                        .description("사용자가 업로드한 이미지에서 배경을 제거하고 결과 이미지를 반환한다.")
                                        /* 스니펫 적용 불가로 인해 추후 수정
                                        .requestFields(
                                                fieldWithPath("image").type(JsonFieldType.STRING).description("배경 제거를 위한 이미지 파일")
                                        )*/
                                        .responseHeaders(
                                                headerWithName("Content-Type").description("응답의 콘텐츠 타입 (image/png)")
                                        )
                                        .build()
                        )
                ));
    }

    @Test
    @DisplayName("이미지 생성 API 테스트")
    @WithMockUser
    public void generateImageTest() throws Exception {
        MockMultipartFile imageFile = createMockImageFile();
        Resource resource = new ByteArrayResource("generated image data".getBytes());
        Mockito.when(aiService.generateImage(Mockito.any(MultipartFile.class)))
                .thenReturn(ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource));

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.multipart("/api/v1/ai/generate")
                        .file(imageFile)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        result
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document(
                        "ai/generate-image",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(
                                ResourceSnippetParameters.builder()
                                        .tag("AI")
                                        .description("사용자가 업로드한 포스터 이미지를 기반으로 생성된 이미지를 반환한다.")
                                        /* 스니펫 적용 불가로 인해 추후 수정
                                        .requestFields(
                                                fieldWithPath("image").type(JsonFieldType.STRING).description("이미지 생성을 위한 포스터 이미지 파일")
                                        )*/
                                        .responseHeaders(
                                                headerWithName("Content-Type").description("응답의 콘텐츠 타입 (image/png)")
                                        )
                                        .build()
                        )
                ));
    }
}