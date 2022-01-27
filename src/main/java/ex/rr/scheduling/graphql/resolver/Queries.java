package ex.rr.scheduling.graphql.resolver;

import ex.rr.scheduling.EntityMapper;
import ex.rr.scheduling.model.CalendarEntity;
import ex.rr.scheduling.model.UserEntity;
import ex.rr.scheduling.repository.CalendarRepository;
import ex.rr.scheduling.repository.UserRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class Queries {

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @GraphQLQuery(name = "getUserById")
    public Optional<UserEntity> getUserById(@GraphQLArgument(name = "userId") Integer userId) {
        return userRepository.findById(userId);
    }

    @GraphQLQuery(name = "getAllUsers")
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @GraphQLQuery(name = "getCalendar")
    public List<CalendarEntity> getCalendar() {
        return calendarRepository.findAll();
    }

    @GraphQLQuery(name = "getCalendarByUserId")
    public List<CalendarEntity> getCalendarByUserId(@GraphQLArgument(name = "userId") Integer userId) {
        return calendarRepository.findByHoursUsersId(userId);
    }

}
