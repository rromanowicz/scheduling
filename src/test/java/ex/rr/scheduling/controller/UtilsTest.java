package ex.rr.scheduling.controller;

import static ex.rr.scheduling.controller.Utils.cleanUserList;
import static ex.rr.scheduling.controller.Utils.hasAdminRole;
import static ex.rr.scheduling.controller.Utils.hasModeratorRole;
import static ex.rr.scheduling.controller.Utils.parseResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import ex.rr.scheduling.model.Location;
import ex.rr.scheduling.model.Session;
import ex.rr.scheduling.model.SessionMonth;
import ex.rr.scheduling.model.SessionYear;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@TestInstance(Lifecycle.PER_CLASS)
public class UtilsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void shouldHaveAdminRightsWhileIsAdmin() {
        assertTrue(hasAdminRole());
    }

    @Test
    @WithMockUser(roles = {"USER", "MODERATOR"})
    public void shouldNotHaveAdminRightsWhileIsNotAdmin() {
        assertFalse(hasAdminRole());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void shouldHaveModeratorRightsWhileIsAdmin() {
        assertTrue(hasModeratorRole());
    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    public void shouldHaveModeratorRightsWhileIsModerator() {
        assertTrue(hasModeratorRole());
    }

    @Test
    @WithMockUser()
    public void shouldNotHaveModeratorRightsWhileIsUser() {
        assertFalse(hasModeratorRole());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void shouldShowAdminFieldsWhileIsAdmin() throws IOException {
        List<Location> location = List.of(
                objectMapper.readValue(new File(
                                Objects.requireNonNull(getClass().getClassLoader().getResource("location.json")).getFile()),
                        Location[].class));

        String parsedLocation = parseResponse(location);

        assertTrue(parsedLocation.contains("email"));
        assertTrue(parsedLocation.contains("roles"));
        assertTrue(parsedLocation.contains("settings"));
    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    public void shouldNotShowAdminFieldsWhileIsModerator() throws IOException {
        List<Location> location = List.of(
                objectMapper.readValue(new File(
                                Objects.requireNonNull(getClass().getClassLoader().getResource("location.json")).getFile()),
                        Location[].class));

        String parsedLocation = parseResponse(location);

        assertFalse(parsedLocation.contains("email"));
        assertFalse(parsedLocation.contains("roles"));
        assertTrue(parsedLocation.contains("settings"));
    }

    @Test
    @WithMockUser()
    public void shouldNotShowAdminAndModeratorFieldsWhileIsUser() throws IOException {
        List<Location> location = List.of(
                objectMapper.readValue(new File(
                                Objects.requireNonNull(getClass().getClassLoader().getResource("location.json")).getFile()),
                        Location[].class));

        String parsedLocation = parseResponse(location);

        assertFalse(parsedLocation.contains("email"));
        assertFalse(parsedLocation.contains("roles"));
        assertFalse(parsedLocation.contains("settings"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void shouldShowAllUsersWhileIsAdmin() throws IOException {
        SessionMonth sessionMonth =
                objectMapper.readValue(new File(
                                Objects.requireNonNull(getClass().getClassLoader().getResource("sessionMonth.json")).getFile()),
                        SessionMonth.class);

        SessionMonth parsedMonth = cleanUserList(sessionMonth);
        Session session = parsedMonth.getSessionDays().get(0).getSessions().get(0);

        assertEquals(2, session.getUsers().size());
        assertEquals(2, session.getCount());
    }

    @Test
    @WithMockUser(roles = {"MODERATOR"})
    public void shouldShowAllUsersWhileIsModerator() throws IOException {
        SessionMonth sessionMonth =
                objectMapper.readValue(new File(
                                Objects.requireNonNull(getClass().getClassLoader().getResource("sessionMonth.json")).getFile()),
                        SessionMonth.class);

        SessionMonth parsedMonth = cleanUserList(sessionMonth);
        Session session = parsedMonth.getSessionDays().get(0).getSessions().get(0);

        assertEquals(2, session.getUsers().size());
        assertEquals(2, session.getCount());
    }

    @Test
    @WithMockUser(username = "test3")
    public void shouldNotShowOtherUsers() throws IOException {
        SessionMonth sessionMonth =
                objectMapper.readValue(new File(
                                Objects.requireNonNull(getClass().getClassLoader().getResource("sessionMonth.json")).getFile()),
                        SessionMonth.class);

        SessionMonth parsedMonth = cleanUserList(sessionMonth);
        Session session = parsedMonth.getSessionDays().get(0).getSessions().get(0);

        assertEquals(1, session.getUsers().size());
        assertEquals(2, session.getCount());
    }

}
