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
import ex.rr.scheduling.model.Location;
import ex.rr.scheduling.repository.LocationRepository;
import ex.rr.scheduling.repository.SettingsRepository;
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
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private SettingsRepository settingsRepository;


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
    @WithMockUser()
    public void shouldReturnOkWhenGetLocations() throws Exception {
        this.mockMvc.perform(get("/location")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(locationRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser()
    public void shouldReturnOkWhenGetLocationById() throws Exception {
        this.mockMvc.perform(get("/location/1")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(locationRepository, times(1)).findAllById(anyList());
    }

    @Test
    @WithMockUser(roles = {"MODERATOR", "USER"})
    public void shouldReturnForbiddenWhenAddLocationWhileIsNotAdmin() throws Exception {
        this.mockMvc.perform(get("/location/add")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Test Location")
                        .characterEncoding("utf-8"))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(locationRepository, never()).save(any(Location.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void shouldReturnOkWhenAddLocationWhileIsAdmin() throws Exception {
        this.mockMvc.perform(post("/location/add")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Test Location")
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andReturn();

        verify(locationRepository, times(1)).save(any(Location.class));
    }


    @Test
    @WithMockUser(roles = {"MODERATOR", "USER"})
    public void shouldReturnForbiddenWhenAddSettingsWhileIsNotAdmin() throws Exception {
        this.mockMvc.perform(get("/location/1/addSettings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\n"
                                + "\t{\n"
                                + "\t\t\"id\": null,\n"
                                + "\t\t\"type\": \"SESSION\",\n"
                                + "\t\t\"subType\": \"MAX_USERS\",\n"
                                + "\t\t\"val\": \"5\"\n"
                                + "\t}\n"
                                + "]")
                        .characterEncoding("utf-8"))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(locationRepository, never()).save(any(Location.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void shouldReturnOkWhenAddSettingsWhileIsAdmin() throws Exception {
        this.mockMvc.perform(post("/location/1/addSettings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\n"
                                + "\t{\n"
                                + "\t\t\"id\": null,\n"
                                + "\t\t\"type\": \"SESSION\",\n"
                                + "\t\t\"subType\": \"MAX_USERS\",\n"
                                + "\t\t\"val\": \"5\"\n"
                                + "\t}\n"
                                + "]")
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andReturn();

        verify(settingsRepository, times(1)).saveAll(anyList());
    }

}
