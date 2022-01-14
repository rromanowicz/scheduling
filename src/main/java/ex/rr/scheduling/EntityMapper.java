package ex.rr.scheduling;

import ex.rr.scheduling.model.RoleEnum;
import ex.rr.scheduling.model.UserEntity;
import ex.rr.scheduling.model.graphql.UserMutation;
import ex.rr.scheduling.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
