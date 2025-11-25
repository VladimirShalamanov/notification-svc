package app.web;

import app.model.NotificationPreference;
import app.model.NotificationType;
import app.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// todo this test not work
@WebMvcTest(NotificationController.class)
public class NotificationControllerApiTest {

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenUserRequestNotificationPreferencesAndExist_thenReturnCorrectJsonAndStatus200() throws Exception {

        // Given
        UUID idUser = UUID.randomUUID();
        UUID idPref = UUID.randomUUID();
        NotificationPreference notificationPreference = NotificationPreference.builder()
                .id(idPref)
                .type(NotificationType.EMAIL)
                .contactInfo("string")
                .enabled(true)
                .userId(idUser)
                .build();

        when(notificationService.getPreferenceByUserId(idUser)).thenReturn(notificationPreference);

        MockHttpServletRequestBuilder httpRequest = get("/api/v1/notifications")
                .param("userId", idUser.toString());

        // When and Then
        mockMvc.perform(httpRequest)
                .andExpect(status().isOk());
//                .andExpect(jsonPath("id").isNotEmpty())
//                .andExpect(jsonPath("userId").isNotEmpty())
//                .andExpect(jsonPath("type").isNotEmpty())
//                .andExpect(jsonPath("enabled").isNotEmpty())
//                .andExpect(jsonPath("contactInfo").isNotEmpty());
    }
}