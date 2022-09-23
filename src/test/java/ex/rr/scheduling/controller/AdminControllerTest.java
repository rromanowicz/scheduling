package ex.rr.scheduling.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ex.rr.scheduling.model.SessionDay;
import ex.rr.scheduling.model.User;
import ex.rr.scheduling.repository.SessionDayRepository;
import ex.rr.scheduling.repository.SessionRepository;
import ex.rr.scheduling.repository.UserRepository;
import java.util.ArrayList;
import java.util.Optional;
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
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private SessionDayRepository sessionDayRepository;


    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void shouldReturnOkWhenLockUserWhileIsAdmin() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(User.builder().id(1).username("test").build()));

        this.mockMvc.perform(patch("/admin/user/1/enabled?enabled=false")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @WithMockUser(roles = {"MODERATOR", "USER"})
    public void shouldReturnForbiddenWhenLockUserWhileNotAdmin() throws Exception {
        this.mockMvc.perform(patch("/admin/user/1/enabled?enabled=false")
                        .characterEncoding("utf-8"))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void shouldReturnOkWhenAddSessionWhileIsAdmin() throws Exception {
        when(sessionDayRepository.findById(1)).thenReturn(
                Optional.ofNullable(SessionDay.builder().id(1).sessions(new ArrayList<>()).build()));

        this.mockMvc.perform(post("/admin/session/1/addSession?sessionTime=14:00")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();

        verify(sessionDayRepository, times(1)).save(any(SessionDay.class));
    }

    @Test
    @WithMockUser(roles = {"MODERATOR", "USER"})
    public void shouldReturnForbiddenWhenAddSessionWhileNotAdmin() throws Exception {
        this.mockMvc.perform(post("/admin/session/1/addSession?sessionTime=14:00")
                        .characterEncoding("utf-8"))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(sessionDayRepository, never()).save(any(SessionDay.class));
    }


}
