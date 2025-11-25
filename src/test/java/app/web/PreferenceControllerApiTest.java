package app.web;

import app.model.NotificationPreference;
import app.model.NotificationType;
import app.service.NotificationPreferenceService;
import app.web.dto.PreferenceRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

@WebMvcTest(PreferenceController.class)
public class PreferenceControllerApiTest {

    @MockitoBean
    private NotificationPreferenceService preferenceService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postUpsertPreference_shouldInvokeServiceMethodAndReturn201CreatedAndReturnPreferenceResponse() throws Exception {

        // 1. Prepare the request dto
        PreferenceRequest dto = PreferenceRequest.builder()
                .userId(UUID.randomUUID())
                .contactInfo("test@gmail.com")
                .notificationEnabled(true)
                .build();

        NotificationPreference entity = NotificationPreference.builder()
                .type(NotificationType.EMAIL)
                .contactInfo("string")
                .enabled(true)
                .build();

        when(preferenceService.upsert(any())).thenReturn(entity);

        // 2. Build the http request
        MockHttpServletRequestBuilder httpRequest = post("/api/v1/preferences")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsBytes(dto));

        // 3. Send request
        mockMvc.perform(httpRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("type").isNotEmpty())
                .andExpect(jsonPath("notificationEnabled").isNotEmpty())
                .andExpect(jsonPath("contactInfo").isNotEmpty())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(preferenceService).upsert(any());
    }
}
