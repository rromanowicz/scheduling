package ex.rr.scheduling.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import ex.rr.scheduling.model.Role;
import ex.rr.scheduling.model.Session;
import ex.rr.scheduling.model.Settings;
import ex.rr.scheduling.model.User;
import ex.rr.scheduling.model.enums.RoleEnum;
import ex.rr.scheduling.model.enums.SettingsSubTypeEnum;
import ex.rr.scheduling.model.enums.SettingsTypeEnum;
import ex.rr.scheduling.repository.SessionRepository;
import ex.rr.scheduling.repository.SettingsRepository;
import ex.rr.scheduling.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private SettingsRepository settingsRepository;


    @BeforeEach
    public void setup() throws IOException {
        Session session =
                objectMapper.readValue(new File(
                                Objects.requireNonNull(getClass().getClassLoader().getResource("session.json")).getFile()),
                        Session.class);

        when(sessionRepository.findById(1)).thenReturn(Optional.ofNullable(session));
        when(settingsRepository.findByLocationIdAndSubType(1, SettingsSubTypeEnum.MAX_USERS)).thenReturn(
                List.of(Settings.builder().type(SettingsTypeEnum.SESSION).subType(SettingsSubTypeEnum.MAX_USERS).val(
                        "3").build())
        );
        when(userRepository.findByUsername("test1")).thenReturn(getUser());
        when(userRepository.findByUsername("test2")).thenReturn(getUser());
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void shouldReturnOkWhenAddUserSessionWhileIsAdmin() throws Exception {
        this.mockMvc.perform(post("/location/1/session/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"sessionId\": 1,\n"
                                + "\t\"username\": \"test1\"\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @WithMockUser(username = "test", roles = {"MODERATOR"})
    public void shouldReturnOkWhenAddUserSessionWhileIsMod() throws Exception {
        this.mockMvc.perform(post("/location/1/session/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"sessionId\": 1,\n"
                                + "\t\"username\": \"test1\"\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @WithMockUser(username = "test1")
    public void shouldReturnOkWhenAddUserSessionWhileIsUser() throws Exception {
        this.mockMvc.perform(post("/location/1/session/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"sessionId\": 1,\n"
                                + "\t\"username\": \"test1\"\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @WithMockUser(username = "test1")
    public void shouldReturnUnauthorizedWhenAddAnotherUserSessionWhileIsUser() throws Exception {
        this.mockMvc.perform(post("/location/1/session/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"sessionId\": 1,\n"
                                + "\t\"username\": \"test2\"\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isUnauthorized())
                .andReturn();

        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void shouldReturnOkWhenDeleteUserSessionWhileIsAdmin() throws Exception {
        this.mockMvc.perform(delete("/location/1/session/deleteUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"sessionId\": 1,\n"
                                + "\t\"username\": \"test1\"\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @WithMockUser(username = "test", roles = {"MODERATOR"})
    public void shouldReturnOkWhenDeleteUserSessionWhileIsMod() throws Exception {
        this.mockMvc.perform(delete("/location/1/session/deleteUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"sessionId\": 1,\n"
                                + "\t\"username\": \"test1\"\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @WithMockUser(username = "test1")
    public void shouldReturnOkWhenDeleteUserSessionWhileIsUser() throws Exception {
        this.mockMvc.perform(delete("/location/1/session/deleteUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"sessionId\": 1,\n"
                                + "\t\"username\": \"test1\"\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @WithMockUser(username = "test1")
    public void shouldReturnUnauthorizedWhenDeleteAnotherUserSessionWhileIsUser() throws Exception {
        this.mockMvc.perform(delete("/location/1/session/deleteUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n"
                                + "\t\"sessionId\": 1,\n"
                                + "\t\"username\": \"test2\"\n"
                                + "}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isUnauthorized())
                .andReturn();

        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void shouldReturnOkWhenLockSessionWhileIsAdmin() throws Exception {
        this.mockMvc.perform(patch("/location/1/session/1/lock")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER", "MODERATOR"})
    public void shouldReturnForbiddenWhenLockSessionWhileIsNotAdmin() throws Exception {
        this.mockMvc.perform(patch("/location/1/session/1/lock")
                        .characterEncoding("utf-8"))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    @WithMockUser(username = "test", roles = {"ADMIN"})
    public void shouldReturnOkWhenUnlockSessionWhileIsAdmin() throws Exception {
        this.mockMvc.perform(patch("/location/1/session/1/unlock")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER", "MODERATOR"})
    public void shouldReturnForbiddenWhenUnlockSessionWhileIsNotAdmin() throws Exception {
        this.mockMvc.perform(patch("/location/1/session/1/unlock")
                        .characterEncoding("utf-8"))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(sessionRepository, never()).save(any(Session.class));
    }


    private static Optional<User> getUser() {
        return Optional.ofNullable(
                User.builder().username("test1").roles(Set.of(new Role(RoleEnum.ROLE_USER))).build());
    }
}
