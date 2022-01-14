package ex.rr.scheduling.service;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import ex.rr.scheduling.EntityMapper;
import ex.rr.scheduling.model.CalendarEntity;
import ex.rr.scheduling.model.UserEntity;
import ex.rr.scheduling.model.graphql.CalendarMutation;
import ex.rr.scheduling.repository.CalendarRepository;
import ex.rr.scheduling.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class SchedulingService implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private UserService userService;

    public String addSession(Integer userId, CalendarMutation calendar) {
        Optional<UserEntity> user = userService.getUserById(userId);
        Optional<CalendarEntity> calendarEntity = calendarRepository.findBySessionDateAndSessionTime(calendar.getSessionDate(), calendar.getSessionTime());
        if (calendarEntity.isPresent() && user.isPresent()) {
            calendarEntity.get().addUser(user.get());
            calendarRepository.save(calendarEntity.get());
            return String.format("Session saved {username:'%s', session: {date: '%s', time: '%s'}}",
                    user.get().getUsername(), calendar.getSessionDate(), calendar.getSessionDate());
        }
        return null;
    }

    public String cancelSession(Integer userId, CalendarMutation calendar) {
        Optional<UserEntity> user = userService.getUserById(userId);
        Optional<CalendarEntity> calendarEntity = calendarRepository.findBySessionDateAndSessionTime(calendar.getSessionDate(), calendar.getSessionTime());
        if (calendarEntity.isPresent() && user.isPresent()) {
            calendarEntity.get().removeUser(user.get());
            calendarRepository.save(calendarEntity.get());
            return "DONE!";
        }
        return "I CAN'T DO THAT!";
    }
}
