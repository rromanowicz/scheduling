package ex.rr.scheduling.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ex.rr.scheduling.model.Location;
import ex.rr.scheduling.model.SessionMonth;
import ex.rr.scheduling.model.View;
import ex.rr.scheduling.model.enums.RoleEnum;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class Utils {

    static boolean hasModeratorRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(RoleEnum.ROLE_MODERATOR.name()) ||
                        r.getAuthority().equals(RoleEnum.ROLE_ADMIN.name())
                );
    }

    static boolean hasAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(RoleEnum.ROLE_ADMIN.name()));
    }

    static String parseResponse(Object input) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        if (hasAdminRole()) {
            return objectMapper.writerWithView(View.IAdmin.class).writeValueAsString(input);
        }
        if (hasModeratorRole()) {
            return objectMapper.writerWithView(View.IModerator.class).writeValueAsString(input);
        } else {
            return objectMapper.writerWithView(View.ILocation.class).writeValueAsString(input);
        }
    }

    static List<Location> cleanOutput(List<Location> locations) {

        if (!hasModeratorRole()) {
            locations.forEach(location ->
                    location.getSessionYears().forEach(sessionYear ->
                            sessionYear.getSessionMonths().forEach(Utils::cleanUserList
                            )));
        }

        locations.forEach(location ->
                location.getSessionYears().forEach(sessionYear ->
                        sessionYear.getSessionMonths().removeIf(sessionMonth ->
                                sessionMonth.getMonthDate().isBefore(LocalDate.now().withDayOfMonth(1))
                        )));

        return locations;
    }

    static SessionMonth cleanUserList(SessionMonth sessionMonth) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!hasModeratorRole()) {
            sessionMonth.getSessionDays().forEach(sessionDay ->
                    sessionDay.getSessions().forEach(session ->
                            session.getUsers().removeIf(user ->
                                    !authentication.getName().equals(user.getUsername()))
                    ));
        }
        return sessionMonth;
    }

}
