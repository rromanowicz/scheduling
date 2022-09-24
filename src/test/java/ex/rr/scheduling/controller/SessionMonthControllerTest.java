package ex.rr.scheduling.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import ex.rr.scheduling.model.SessionMonth;
import ex.rr.scheduling.model.SessionYear;
import ex.rr.scheduling.model.Settings;
import ex.rr.scheduling.model.enums.SettingsSubTypeEnum;
import ex.rr.scheduling.model.enums.SettingsTypeEnum;
import ex.rr.scheduling.repository.SessionDayRepository;
import ex.rr.scheduling.repository.SessionMonthRepository;
import ex.rr.scheduling.repository.SessionYearRepository;
import ex.rr.scheduling.repository.SettingsRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class SessionMonthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private SessionYearRepository sessionYearRepository;
    @MockBean
    private SessionMonthRepository sessionMonthRepository;
    @MockBean
    private SessionDayRepository sessionDayRepository;
    @MockBean
    private SettingsRepository settingsRepository;


    @BeforeEach
    public void setup() throws IOException {
        SessionYear sessionYear =
                objectMapper.readValue(new File(
                                Objects.requireNonNull(getClass().getClassLoader().getResource("sessionYear.json")).getFile()),
                        SessionYear.class);

        when(sessionYearRepository.findByLocationIdAndSessionYear(1, 2022)).thenReturn(
                Optional.ofNullable(sessionYear));
        when(sessionYearRepository.save(any(SessionYear.class))).thenReturn(
                SessionYear.builder().id(1).sessionYear(2023).sessionMonths(new ArrayList<>()).build());
        when(sessionMonthRepository.save(any(SessionMonth.class))).thenReturn(SessionMonth.builder().id(1).build());
        when(settingsRepository.findByLocationIdAndSubType(1, SettingsSubTypeEnum.MAX_USERS)).thenReturn(
                List.of(Settings.builder().type(SettingsTypeEnum.SESSION).subType(SettingsSubTypeEnum.HOUR).val(
                        "13:00").build())
        );
    }


    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void shouldReturnOkWhenAddSessionMonthWhileIsAdmin() throws Exception {
        this.mockMvc.perform(post("/location/1/month/add?year=2023&month=10")
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andReturn();

        verify(sessionYearRepository, times(1)).save(any(SessionYear.class));
        verify(sessionMonthRepository, times(2)).save(any(SessionMonth.class));
        verify(sessionDayRepository, times(1)).saveAll(anyList());
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER", "MODERATOR"})
    public void shouldReturnForbiddenWhenAddSessionMonthWhileIsNotAdmin() throws Exception {
        this.mockMvc.perform(post("/location/1/month/add?year=2022&month=9")
                        .characterEncoding("utf-8"))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(sessionYearRepository, never()).save(any(SessionYear.class));
        verify(sessionMonthRepository, never()).save(any(SessionMonth.class));
        verify(sessionDayRepository, never()).saveAll(anyList());
    }

    @Test
    @WithMockUser(username = "test")
    public void shouldReturnOkWhenGetSessionMonth() throws Exception {
        this.mockMvc.perform(get("/location/1/month/2022/9")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();
    }


}
