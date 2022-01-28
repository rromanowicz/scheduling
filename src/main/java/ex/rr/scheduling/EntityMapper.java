package ex.rr.scheduling;

import ex.rr.scheduling.model.*;
import ex.rr.scheduling.model.graphql.UserMutation;
import ex.rr.scheduling.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EntityMapper {

    @Autowired
    private UserRepository userRepository;

    public UserEntity userMtE(UserMutation user) {
        return UserEntity.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .role(RoleEnum.USER)
                .build();
    }

    public UserSessions mapUserSessions(UserEntity userEntity, List<CalendarEntryEntity> calendarEntries) {
        UserSessions userSessions = UserSessions.builder().user(userEntity).sessions(new ArrayList<>()).build();
        calendarEntries.forEach(ce -> {
            ce.getHours().forEach(hr -> {
                if (hr.getUsers().contains(userEntity)) {
                    userSessions.getSessions().add(
                            Session.builder()
                                    .id(hr.getId())
                                    .sessionDate(ce.getSessionDate())
                                    .sessionTime(hr.getSessionTime())
                                    .build());
                }
            });
        });
        return userSessions;
    }
}
