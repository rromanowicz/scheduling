package ex.rr.scheduling.graphql.resolver;

import ex.rr.scheduling.EntityMapper;
import ex.rr.scheduling.model.*;
import ex.rr.scheduling.repository.CalendarEntryRepository;
import ex.rr.scheduling.repository.CalendarRepository;
import ex.rr.scheduling.repository.HostRepository;
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
    private CalendarEntryRepository calendarEntryRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private HostRepository hostRepository;


    @GraphQLQuery(name = "getUserById")
    public Optional<UserEntity> getUserById(@GraphQLArgument(name = "userId") Integer userId) {
        return userRepository.findById(userId);
    }

    @GraphQLQuery(name = "getAllUsers")
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @GraphQLQuery(name = "getUserSessions")
    public UserSessions getUserSessions(@GraphQLArgument(name = "userId") Integer userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        return userEntity.map(entity -> entityMapper.mapUserSessions(entity, calendarEntryRepository.findByHoursUsersId(userId))).orElse(null);
    }

    @GraphQLQuery(name = "getCalendar")
    public CalendarEntity getCalendar(@GraphQLArgument(name = "id") Integer id) {
        return calendarRepository.findById(id).orElse(null);
    }

    @GraphQLQuery(name = "getHostById")
    public HostEntity getHostById(@GraphQLArgument(name = "hostId") Integer hostId) {
        return hostRepository.findByHostId(hostId).orElse(null);
    }

}
