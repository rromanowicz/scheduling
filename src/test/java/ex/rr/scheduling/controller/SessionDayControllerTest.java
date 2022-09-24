package ex.rr.scheduling.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import ex.rr.scheduling.model.Location;
import ex.rr.scheduling.repository.LocationRepository;
import ex.rr.scheduling.repository.SessionDayRepository;
import java.io.File;
import java.io.IOException;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class SessionDayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private SessionDayRepository sessionDayRepository;


    @BeforeEach
    public void setup() throws IOException {
        List<Location> location = List.of(
                objectMapper.readValue(new File(
                                Objects.requireNonNull(getClass().getClassLoader().getResource("location.json")).getFile()),
                        Location[].class));

        when(locationRepository.findAll()).thenReturn(location);
        when(locationRepository.findAllById(List.of(1))).thenReturn(location);
        when(locationRepository.findById(1)).thenReturn(Optional.ofNullable(location.get(0)));
    }


    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void shouldReturnOkWhenLockSessionWhileIsAdmin() throws Exception {
        this.mockMvc.perform(patch("/location/1/sessionDays/lock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"dateFrom\": \"2022-09-01\",\n"
                                + "\t\"dateTo\": \"2022-09-01\"\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(sessionDayRepository, times(1)).saveAll(anyList());
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER", "MODERATOR"})
    public void shouldReturnForbiddenWhenLockSessionWhileIsNotAdmin() throws Exception {
        this.mockMvc.perform(patch("/location/1/sessionDays/lock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"dateFrom\": \"2022-09-01\",\n"
                                + "\t\"dateTo\": \"2022-09-05\"\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(sessionDayRepository, never()).saveAll(anyList());
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void shouldReturnOkWhenUnlockSessionWhileIsAdmin() throws Exception {
        this.mockMvc.perform(patch("/location/1/sessionDays/unlock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"dateFrom\": \"2022-09-01\",\n"
                                + "\t\"dateTo\": \"2022-09-05\"\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(sessionDayRepository, times(1)).saveAll(anyList());
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER", "MODERATOR"})
    public void shouldReturnForbiddenWhenUnlockSessionWhileIsNotAdmin() throws Exception {
        this.mockMvc.perform(patch("/location/1/sessionDays/unlock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"dateFrom\": \"2022-09-01\",\n"
                                + "\t\"dateTo\": \"2022-09-05\"\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(sessionDayRepository, never()).saveAll(anyList());
    }

}
