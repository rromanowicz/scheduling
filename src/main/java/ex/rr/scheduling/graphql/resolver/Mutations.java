package ex.rr.scheduling.graphql.resolver;

import ex.rr.scheduling.EntityMapper;
import ex.rr.scheduling.model.CalendarEntity;
import ex.rr.scheduling.model.HourEntity;
import ex.rr.scheduling.model.UserEntity;
import ex.rr.scheduling.model.graphql.CalendarMutation;
import ex.rr.scheduling.model.graphql.UserMutation;
import ex.rr.scheduling.repository.CalendarRepository;
import ex.rr.scheduling.repository.UserRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@AllArgsConstructor
public class Mutations {

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private Queries queries;

    @GraphQLMutation(name = "addUser")
    public UserEntity addUser(@GraphQLArgument(name = "userInput") UserMutation userInput) {
        return userRepository.save(entityMapper.userMtE(userInput));
    }

//    @Transactional
    @GraphQLMutation(name = "updateUserEmail")
    public UserEntity updateUserEmail(@GraphQLArgument(name = "userId") Integer userId,
                                      @GraphQLArgument(name = "email") String email) {
        UserEntity user = userRepository.getOne(userId);
        user.setEmail(email);
        return userRepository.save(user);
    }

    @GraphQLMutation(name = "addSession")
    public String addSession(@GraphQLArgument(name = "userId") Integer userId,
                             @GraphQLArgument(name = "calendarInput") CalendarMutation calendar) {
        Optional<UserEntity> user = queries.getUserById(userId);
        Optional<CalendarEntity> calendarEntity = calendarRepository.findBySessionDate(calendar.getSessionDate());
        AtomicBoolean isSuccess = new AtomicBoolean(false);
        if (calendarEntity.isPresent() && user.isPresent()) {
            calendarEntity.get().getHours().forEach(it ->
            {
                if (it.getSessionTime().equals(calendar.getSessionTime())) {
                    it.addUser(user.get());
                }
            });
            calendarRepository.save(calendarEntity.get());
            isSuccess.set(true);

            if (isSuccess.get()) {
                return String.format("Session saved {username:'%s', session: {date: '%s', time: '%s'}}",
                        user.get().getUsername(), calendar.getSessionDate(), calendar.getSessionDate());
            }
        }
        return "Nope.";
    }

    @GraphQLMutation(name = "cancelSession")
    public String cancelSession(@GraphQLArgument(name = "userId") Integer userId,
                                @GraphQLArgument(name = "calendarInput") CalendarMutation calendar) {
        Optional<UserEntity> user = queries.getUserById(userId);
        Optional<CalendarEntity> calendarEntity = calendarRepository.findBySessionDate(calendar.getSessionDate());
        if (calendarEntity.isPresent() && user.isPresent()) {
            calendarEntity.get().getHours().forEach(it ->
            {
                if (it.getSessionTime().equals(calendar.getSessionTime())) {
                    it.removeUser(user.get());
                }
            });
            calendarRepository.save(calendarEntity.get());
            return "DONE!";
        }
        return "I CAN'T DO THAT!";
    }

}
